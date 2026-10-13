package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ui.AppScreen
import com.example.ui.CarViewModel
import com.example.ui.components.ApiKeyDialog
import com.example.ui.components.AutomotiveBottomNav
import com.example.ui.components.AutomotiveTopBar
import com.example.ui.screens.GarageHistoryScreen
import com.example.ui.screens.ScannerScreen
import com.example.ui.screens.ShowcaseScreen
import com.example.ui.theme.CarbonDark
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: CarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainContent(viewModel: CarViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val scanHistory by viewModel.scanHistory.collectAsState()
    val selectedUri by viewModel.selectedUri.collectAsState()
    val selectedBitmap by viewModel.selectedBitmap.collectAsState()
    val selectedSampleCar by viewModel.selectedSampleCar.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val apiKeyInfo by viewModel.apiKeyInfo.collectAsState()

    var showApiKeyDialog by remember { mutableStateOf(false) }

    // Handle back button on secondary screens
    if (currentScreen != AppScreen.SCANNER) {
        BackHandler {
            viewModel.navigateTo(AppScreen.SCANNER)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(CarbonDark),
        topBar = {
            AutomotiveTopBar(
                historyCount = scanHistory.size,
                hasApiKey = apiKeyInfo.hasKey,
                onOpenApiKeyDialog = { showApiKeyDialog = true }
            )
        },
        bottomBar = {
            AutomotiveBottomNav(
                currentScreen = currentScreen,
                historyCount = scanHistory.size,
                onNavigate = { viewModel.navigateTo(it) }
            )
        },
        containerColor = CarbonDark
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.SCANNER -> {
                    ScannerScreen(
                        selectedUri = selectedUri,
                        selectedBitmap = selectedBitmap,
                        selectedSampleCar = selectedSampleCar,
                        uiState = uiState,
                        hasApiKey = apiKeyInfo.hasKey,
                        onSelectUri = { viewModel.selectImageUri(it) },
                        onSelectBitmap = { viewModel.selectBitmap(it) },
                        onSelectSampleCar = { viewModel.selectSampleCar(it) },
                        onGetCameraUri = { viewModel.createCameraUri() },
                        onAnalyze = { viewModel.analyzeCurrentVehicle() },
                        onReset = { viewModel.reset() },
                        onOpenApiKeyDialog = { showApiKeyDialog = true }
                    )
                }
                AppScreen.SHOWCASE -> {
                    ShowcaseScreen(
                        onSelectAndAnalyze = { car ->
                            viewModel.selectSampleCar(car, analyzeImmediately = true)
                            viewModel.navigateTo(AppScreen.SCANNER)
                        }
                    )
                }
                AppScreen.GARAGE -> {
                    GarageHistoryScreen(
                        scanHistory = scanHistory,
                        onDeleteItem = { viewModel.deleteHistoryItem(it) },
                        onClearAll = { viewModel.clearAllHistory() },
                        onNavigateToScanner = { viewModel.navigateTo(AppScreen.SCANNER) }
                    )
                }
            }

            if (showApiKeyDialog) {
                ApiKeyDialog(
                    apiKeyInfo = apiKeyInfo,
                    onSaveKey = { viewModel.updateApiKey(it) },
                    onDismiss = { showApiKeyDialog = false }
                )
            }
        }
    }
}
