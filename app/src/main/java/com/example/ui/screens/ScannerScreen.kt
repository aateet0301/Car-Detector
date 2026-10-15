package com.example.ui.screens

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.model.SampleCar
import com.example.ui.UiState
import com.example.ui.components.CarResultView
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CarbonCard
import com.example.ui.theme.CarbonCardBorder
import com.example.ui.theme.CarbonDark
import com.example.ui.theme.RacingRed
import com.example.ui.theme.SpeedBlue
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ScannerScreen(
    selectedUri: Uri?,
    selectedBitmap: Bitmap?,
    selectedSampleCar: SampleCar?,
    uiState: UiState,
    hasApiKey: Boolean,
    onSelectUri: (Uri) -> Unit,
    onSelectBitmap: (Bitmap) -> Unit,
    onSelectSampleCar: (SampleCar) -> Unit,
    onGetCameraUri: () -> Uri,
    onAnalyze: () -> Unit,
    onReset: () -> Unit,
    onOpenApiKeyDialog: () -> Unit,
    onOverrideModel: (String) -> Unit = {},
    onDismissApiWarning: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }
    var showPermissionDeniedDialog by remember { mutableStateOf(false) }

    // Direct Bitmap Preview Capture (reliable fallback across all devices/emulators)
    val takePicturePreviewLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            onSelectBitmap(bitmap)
        }
    }

    // High-resolution FileProvider TakePicture contract
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && pendingCameraUri != null) {
            onSelectUri(pendingCameraUri!!)
        } else {
            // If FileProvider capture did not return OK, gracefully try direct preview capture
            try {
                takePicturePreviewLauncher.launch(null)
            } catch (_: Exception) {
                // User may have backed out
            }
        }
    }

    fun launchCameraInternal() {
        try {
            val tempUri = onGetCameraUri()
            pendingCameraUri = tempUri
            takePictureLauncher.launch(tempUri)
        } catch (e: ActivityNotFoundException) {
            try {
                takePicturePreviewLauncher.launch(null)
            } catch (_: Exception) {
                Toast.makeText(
                    context,
                    "No camera application found. Please choose a photo from your Gallery.",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            try {
                takePicturePreviewLauncher.launch(null)
            } catch (_: Exception) {
                Toast.makeText(
                    context,
                    "Could not launch camera: ${e.localizedMessage ?: "Unknown error"}. You can choose a photo from Gallery.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // Camera runtime permission launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launchCameraInternal()
        } else {
            showPermissionDeniedDialog = true
        }
    }

    fun handleCameraClick() {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            launchCameraInternal()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Universal Android Gallery picker contract
    val galleryPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            onSelectUri(uri)
        }
    }

    val hasImage = selectedUri != null || selectedBitmap != null

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // API Key Status Banner (optional notice)
        if (!hasApiKey) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .testTag("api_key_banner"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2838)),
                    border = BorderStroke(1.dp, AmberWarning.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(AmberWarning.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Key,
                                contentDescription = "API Key",
                                tint = AmberWarning,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Gemini Vision Multimodal API",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                            Text(
                                text = "On-device automotive spec engine active. Tap 'Set Key' to use your personal Gemini key.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                        Button(
                            onClick = onOpenApiKeyDialog,
                            modifier = Modifier.testTag("btn_banner_set_key"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SpeedBlue,
                                contentColor = Color(0xFF002238)
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Set Key", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Camera Viewfinder / Preview Card
        item {
            ViewfinderCard(
                selectedUri = selectedUri,
                selectedBitmap = selectedBitmap,
                isAnalyzing = uiState is UiState.Analyzing
            )
        }

        // Action Buttons Row: Camera, Gallery, Clear
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = { handleCameraClick() },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("btn_take_photo"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = SpeedBlue),
                    border = BorderStroke(1.dp, CarbonCardBorder)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Take Photo",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Camera", fontWeight = FontWeight.SemiBold)
                }

                OutlinedButton(
                    onClick = {
                        try {
                            galleryPickerLauncher.launch("image/*")
                        } catch (e: Exception) {
                            Toast.makeText(context, "Could not open Gallery: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("btn_pick_gallery"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = SpeedBlue),
                    border = BorderStroke(1.dp, CarbonCardBorder)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = "Pick Photo",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Upload Photo", fontWeight = FontWeight.SemiBold)
                }

                if (hasImage) {
                    OutlinedButton(
                        onClick = onReset,
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("btn_reset_selection"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextMuted),
                        border = BorderStroke(1.dp, CarbonCardBorder)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Primary Analyze CTA Button
        item {
            val isAnalyzing = uiState is UiState.Analyzing
            Button(
                onClick = onAnalyze,
                enabled = !isAnalyzing && hasImage,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp)
                    .testTag("btn_analyze_vehicle"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SpeedBlue,
                    contentColor = Color(0xFF002238),
                    disabledContainerColor = SpeedBlue.copy(alpha = 0.35f),
                    disabledContentColor = Color(0xFF002238).copy(alpha = 0.5f)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                if (isAnalyzing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color(0xFF002238),
                        strokeWidth = 2.5.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Analyzing Vehicle...",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (hasImage) "Identify Vehicle & Specifications" else "Upload a Car Photo to Identify",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Error message if any
        if (uiState is UiState.Error) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .testTag("error_card"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = CarbonCard),
                    border = BorderStroke(1.dp, RacingRed)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Error",
                                tint = RacingRed
                            )
                            Text(
                                text = (uiState as UiState.Error).message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }
        }

        // Success Result Display — Systematic Specs WITH Prominent Car Image
        if (uiState is UiState.Success) {
            item {
                CarResultView(
                    result = (uiState as UiState.Success).result,
                    scannedUri = selectedUri,
                    scannedBitmap = selectedBitmap,
                    onOverrideModel = onOverrideModel,
                    onOpenApiKeyDialog = onOpenApiKeyDialog,
                    onDismissApiWarning = onDismissApiWarning,
                    onReset = onReset
                )
            }
        }
    }

    // Permission Denied Explanation Dialog
    if (showPermissionDeniedDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDeniedDialog = false },
            title = {
                Text(
                    text = "Camera Permission Needed",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
            },
            text = {
                Text(
                    text = "AutoLens AI needs camera access to take live photos of cars. You can grant camera permission, or simply upload photos from your Gallery.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showPermissionDeniedDialog = false
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SpeedBlue, contentColor = Color(0xFF002238))
                ) {
                    Text("Grant Permission", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showPermissionDeniedDialog = false
                        galleryPickerLauncher.launch("image/*")
                    }
                ) {
                    Text("Use Gallery Instead", color = SpeedBlue)
                }
            },
            containerColor = CarbonCard
        )
    }
}

@Composable
private fun ViewfinderCard(
    selectedUri: Uri?,
    selectedBitmap: Bitmap?,
    isAnalyzing: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .aspectRatio(16f / 10f)
            .testTag("scanner_viewfinder"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = CarbonCard),
        border = BorderStroke(1.5.dp, SpeedBlue.copy(alpha = 0.6f))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                selectedBitmap != null -> {
                    Image(
                        bitmap = selectedBitmap.asImageBitmap(),
                        contentDescription = "Selected photo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                selectedUri != null -> {
                    AsyncImage(
                        model = selectedUri,
                        contentDescription = "Selected photo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(CarbonDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(SpeedBlue.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DirectionsCar,
                                    contentDescription = null,
                                    tint = SpeedBlue,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            Text(
                                text = "Take or upload a car photo to scan",
                                color = TextPrimary,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Text(
                                text = "Front, rear, or side view of any vehicle",
                                color = TextMuted,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // HUD Viewfinder Target Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Top-Left corner bracket
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .size(24.dp)
                        .border(
                            BorderStroke(2.5.dp, SpeedBlue),
                            RoundedCornerShape(topStart = 6.dp)
                        )
                )
                // Top-Right corner bracket
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(24.dp)
                        .border(
                            BorderStroke(2.5.dp, SpeedBlue),
                            RoundedCornerShape(topEnd = 6.dp)
                        )
                )
                // Bottom-Left corner bracket
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .size(24.dp)
                        .border(
                            BorderStroke(2.5.dp, SpeedBlue),
                            RoundedCornerShape(bottomStart = 6.dp)
                        )
                )
                // Bottom-Right corner bracket
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(24.dp)
                        .border(
                            BorderStroke(2.5.dp, SpeedBlue),
                            RoundedCornerShape(bottomEnd = 6.dp)
                        )
                )
            }

            // Vehicle Source Label Tag
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(14.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = when {
                        selectedBitmap != null -> "Camera Capture"
                        selectedUri != null -> "Uploaded Photo"
                        else -> "Vehicle Detection Frame"
                    },
                    color = TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Futuristic Scanner Sweep Animation
            if (isAnalyzing) {
                val infiniteTransition = rememberInfiniteTransition(label = "scanner_sweep")
                val scanOffset by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1400, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "scanner_laser"
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .align(Alignment.TopCenter)
                        .offset(y = (scanOffset * 220).dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    SpeedBlue,
                                    Color.White,
                                    SpeedBlue,
                                    Color.Transparent
                                )
                            )
                        )
                )
            }
        }
    }
}
