package com.example.ui.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.SampleCar
import com.example.ui.UiState
import com.example.ui.components.CarResultView
import com.example.ui.components.RealCarCarousel
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
    modifier: Modifier = Modifier
) {
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }

    // FileProvider TakePicture contract for full 108MP camera capture on Redmi Note 13 5G
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && pendingCameraUri != null) {
            onSelectUri(pendingCameraUri!!)
        }
    }

    // Universal Android Gallery picker contract (works flawlessly on Redmi Note 13 5G, MIUI/HyperOS, and emulators)
    val galleryPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            onSelectUri(uri)
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // API Key Status Banner
        if (!hasApiKey) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .testTag("api_key_banner"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2838)),
                    border = BorderStroke(1.dp, AmberWarning.copy(alpha = 0.6f))
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
                                text = "Gemini API Key Required for Live Photos",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                            Text(
                                text = "Tap 'Set Key' to enter your free Gemini key for instant AI detection on your Redmi device.",
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
                selectedSampleCar = selectedSampleCar,
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
                    onClick = {
                        val tempUri = onGetCameraUri()
                        pendingCameraUri = tempUri
                        takePictureLauncher.launch(tempUri)
                    },
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
                        galleryPickerLauncher.launch("image/*")
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
                    Text("Gallery", fontWeight = FontWeight.SemiBold)
                }

                if (selectedUri != null || selectedBitmap != null) {
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
                enabled = !isAnalyzing,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp)
                    .testTag("btn_analyze_vehicle"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SpeedBlue,
                    contentColor = Color(0xFF002238),
                    disabledContainerColor = SpeedBlue.copy(alpha = 0.5f)
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
                        text = "Analyze Vehicle with AI",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Real Car Showcase Carousel
        item {
            RealCarCarousel(
                selectedCar = selectedSampleCar,
                onCarSelected = onSelectSampleCar
            )
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
                        if (!hasApiKey) {
                            Button(
                                onClick = onOpenApiKeyDialog,
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SpeedBlue, contentColor = Color(0xFF002238))
                            ) {
                                Text("Configure Gemini Key", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Success Result Display
        if (uiState is UiState.Success) {
            item {
                CarResultView(result = (uiState as UiState.Success).result)
            }
        }
    }
}

@Composable
private fun ViewfinderCard(
    selectedUri: Uri?,
    selectedBitmap: Bitmap?,
    selectedSampleCar: SampleCar?,
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
                selectedSampleCar != null -> {
                    Image(
                        painter = painterResource(id = selectedSampleCar.imageRes),
                        contentDescription = selectedSampleCar.title,
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
                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                text = "Take or upload a car photo",
                                color = TextSecondary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            // HUD Viewfinder Target Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
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
                    .background(Color.Black.copy(alpha = 0.65f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = when {
                        selectedSampleCar != null -> "Sample: ${selectedSampleCar.title}"
                        selectedBitmap != null -> "Live Camera Capture"
                        selectedUri != null -> "Camera / Gallery Photo"
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
