package com.example.ui

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CarAnalyzerRepository
import com.example.data.local.CarScanEntity
import com.example.model.CarDataRepository
import com.example.model.CarIdentificationResult
import com.example.model.SampleCar
import com.example.util.ImageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class ApiKeyInfo(
    val hasKey: Boolean,
    val maskedKey: String,
    val isCustom: Boolean,
    val customKey: String
)

sealed interface UiState {
    object Idle : UiState
    object Analyzing : UiState
    data class Success(val result: CarIdentificationResult) : UiState
    data class Error(val message: String) : UiState
}

enum class AppScreen {
    SCANNER,
    SHOWCASE,
    GARAGE
}

class CarViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CarAnalyzerRepository(application)
    private val apiKeyManager = repository.getApiKeyManager()

    val scanHistory: StateFlow<List<CarScanEntity>> = repository.getScanHistory()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _apiKeyInfo = MutableStateFlow(loadApiKeyInfo())
    val apiKeyInfo: StateFlow<ApiKeyInfo> = _apiKeyInfo.asStateFlow()

    private val _currentScreen = MutableStateFlow(AppScreen.SCANNER)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _selectedUri = MutableStateFlow<Uri?>(null)
    val selectedUri: StateFlow<Uri?> = _selectedUri.asStateFlow()

    private val _selectedBitmap = MutableStateFlow<Bitmap?>(null)
    val selectedBitmap: StateFlow<Bitmap?> = _selectedBitmap.asStateFlow()

    private val _selectedSampleCar = MutableStateFlow<SampleCar?>(CarDataRepository.sampleRealCars.firstOrNull())
    val selectedSampleCar: StateFlow<SampleCar?> = _selectedSampleCar.asStateFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private fun loadApiKeyInfo(): ApiKeyInfo {
        val custom = apiKeyManager.getCustomApiKey()
        return ApiKeyInfo(
            hasKey = apiKeyManager.hasValidApiKey(),
            maskedKey = apiKeyManager.getMaskedKey(),
            isCustom = custom.isNotBlank(),
            customKey = custom
        )
    }

    fun updateApiKey(key: String) {
        if (key.isBlank()) {
            apiKeyManager.clearCustomApiKey()
        } else {
            apiKeyManager.setCustomApiKey(key)
        }
        _apiKeyInfo.value = loadApiKeyInfo()
        // Re-analyze if an image is currently selected
        if (_selectedUri.value != null || _selectedBitmap.value != null || _selectedSampleCar.value != null) {
            analyzeCurrentVehicle()
        }
    }

    fun createCameraUri(): Uri {
        return ImageUtils.createCameraTempUri(getApplication())
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun selectSampleCar(sampleCar: SampleCar, analyzeImmediately: Boolean = true) {
        _selectedSampleCar.value = sampleCar
        _selectedUri.value = null
        _selectedBitmap.value = null
        _uiState.value = UiState.Idle
        if (analyzeImmediately) {
            analyzeCurrentVehicle()
        }
    }

    fun selectImageUri(uri: Uri, autoAnalyze: Boolean = true) {
        viewModelScope.launch(Dispatchers.IO) {
            val (cachedUri, bitmap) = ImageUtils.cacheImageAndDecode(getApplication(), uri)
            withContext(Dispatchers.Main) {
                _selectedBitmap.value = bitmap
                _selectedUri.value = cachedUri
                _selectedSampleCar.value = null
                _uiState.value = UiState.Idle
                if (autoAnalyze) {
                    analyzeCurrentVehicle()
                }
            }
        }
    }

    fun selectBitmap(bitmap: Bitmap, autoAnalyze: Boolean = true) {
        _selectedBitmap.value = bitmap
        _selectedSampleCar.value = null
        _selectedUri.value = null
        _uiState.value = UiState.Idle
        if (autoAnalyze) {
            analyzeCurrentVehicle()
        }
    }

    fun analyzeCurrentVehicle() {
        val sample = _selectedSampleCar.value
        val uri = _selectedUri.value
        val bitmap = _selectedBitmap.value

        if (sample == null && uri == null && bitmap == null) {
            _uiState.value = UiState.Error("Please select or capture a car image first")
            return
        }

        viewModelScope.launch {
            _uiState.value = UiState.Analyzing
            val result = repository.analyzeCarImage(
                bitmap = bitmap,
                imageUri = uri,
                sampleResId = sample?.imageRes
            )
            result.onSuccess { identification ->
                _uiState.value = UiState.Success(identification)
            }.onFailure { error ->
                _uiState.value = UiState.Error(error.localizedMessage ?: "Failed to analyze car image")
            }
        }
    }

    fun deleteHistoryItem(item: CarScanEntity) {
        viewModelScope.launch {
            repository.deleteScan(item)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    fun reset() {
        _selectedUri.value = null
        _selectedBitmap.value = null
        _selectedSampleCar.value = CarDataRepository.sampleRealCars.firstOrNull()
        _uiState.value = UiState.Idle
    }
}
