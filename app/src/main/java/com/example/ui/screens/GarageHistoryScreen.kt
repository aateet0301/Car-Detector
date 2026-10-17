package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Garage
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.local.CarScanEntity
import com.example.model.CarIdentificationResult
import com.example.ui.components.CarResultView
import com.example.ui.theme.CarbonCard
import com.example.ui.theme.CarbonCardBorder
import com.example.ui.theme.CarbonDark
import com.example.ui.theme.RacingRed
import com.example.ui.theme.SpeedBlue
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun GarageHistoryScreen(
    scanHistory: List<CarScanEntity>,
    onDeleteItem: (CarScanEntity) -> Unit,
    onClearAll: () -> Unit,
    onNavigateToScanner: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedScanForDetail by remember { mutableStateOf<CarScanEntity?>(null) }
    var showClearConfirmation by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF071B2C)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_cars360_logo),
                            contentDescription = "Cars 360",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Cars 360 Garage",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Text(
                            text = "${scanHistory.size} vehicle${if (scanHistory.size == 1) "" else "s"} scanned & saved",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                if (scanHistory.isNotEmpty()) {
                    IconButton(
                        onClick = { showClearConfirmation = true },
                        modifier = Modifier.testTag("btn_clear_garage")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Clear Garage",
                            tint = TextMuted
                        )
                    }
                }
            }
        }

        if (scanHistory.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CarbonCard),
                    border = BorderStroke(1.dp, CarbonCardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF071B2C))
                                .border(1.dp, SpeedBlue.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_cars360_logo),
                                contentDescription = null,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                        Text(
                            text = "Your Garage is Empty",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Text(
                            text = "Scan any car with the camera or gallery to identify full specifications and save vehicles to your garage.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Button(
                            onClick = onNavigateToScanner,
                            modifier = Modifier.testTag("btn_empty_to_scanner"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SpeedBlue,
                                contentColor = Color(0xFF002238)
                            )
                        ) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Open Scanner", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            items(scanHistory, key = { it.id }) { scan ->
                GarageItemCard(
                    scan = scan,
                    onClick = { selectedScanForDetail = scan },
                    onDelete = { onDeleteItem(scan) }
                )
            }
        }
    }

    // Detail Dialog
    selectedScanForDetail?.let { scan ->
        val result = CarIdentificationResult(
            makeAndModel = scan.makeModel,
            estimatedYearGen = scan.yearGen,
            exteriorColor = scan.exteriorColor,
            colorDetails = com.example.model.CarColorDetails(
                primaryColorName = scan.exteriorColor,
                finishType = scan.colorFinish,
                factoryPaintCode = scan.colorCode,
                shadeTone = scan.colorTone,
                accentDetails = scan.colorAccents,
                hexColor = scan.colorHex
            ),
            bodyStyle = scan.bodyStyle,
            enginePowertrain = scan.enginePowertrain,
            drivetrain = scan.drivetrain,
            horsepower = scan.horsepower,
            notableFeatures = scan.notableFeatures,
            confidenceLevel = scan.confidenceLevel,
            rawFormattedResponse = scan.rawResponse,
            isCarDetected = scan.isCarDetected
        )

        AlertDialog(
            onDismissRequest = { selectedScanForDetail = null },
            confirmButton = {
                TextButton(onClick = { selectedScanForDetail = null }) {
                    Text("Close", color = SpeedBlue, fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Text(
                    text = scan.makeModel,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    CarResultView(result = result)
                }
            },
            containerColor = CarbonCard,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Clear Confirmation Dialog
    if (showClearConfirmation) {
        AlertDialog(
            onDismissRequest = { showClearConfirmation = false },
            title = { Text("Clear Garage History?") },
            text = { Text("This will permanently remove all saved vehicle identifications from your device.") },
            confirmButton = {
                Button(
                    onClick = {
                        onClearAll()
                        showClearConfirmation = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RacingRed)
                ) {
                    Text("Clear All")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirmation = false }) {
                    Text("Cancel")
                }
            },
            containerColor = CarbonCard
        )
    }
}

@Composable
private fun GarageItemCard(
    scan: CarScanEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMM d, yyyy · h:mm a", Locale.getDefault()) }
    val formattedDate = remember(scan.timestamp) { dateFormat.format(Date(scan.timestamp)) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("garage_item_${scan.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CarbonCard),
        border = BorderStroke(1.dp, CarbonCardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Thumbnail
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(CarbonDark)
            ) {
                when {
                    scan.sampleDrawableRes != null -> {
                        Image(
                            painter = painterResource(id = scan.sampleDrawableRes),
                            contentDescription = scan.makeModel,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    scan.imageUri != null -> {
                        AsyncImage(
                            model = scan.imageUri,
                            contentDescription = scan.makeModel,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    else -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null,
                                tint = SpeedBlue,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = scan.makeModel,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary,
                    maxLines = 1
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    val hexColor = parseHexColor(scan.colorHex, Color(0xFF0077C8))
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .background(hexColor)
                            .border(1.dp, Color.White.copy(alpha = 0.4f), androidx.compose.foundation.shape.CircleShape)
                    )
                    Text(
                        text = "${scan.exteriorColor} · ${scan.colorFinish}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF38BDF8),
                        maxLines = 1
                    )
                }
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }

            // Delete action
            IconButton(
                onClick = onDelete,
                modifier = Modifier.testTag("btn_delete_${scan.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = TextMuted,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

private fun parseHexColor(hexString: String, fallback: Color): Color {
    return try {
        val clean = hexString.trim().removePrefix("#")
        if (clean.length == 6) {
            val colorLong = clean.toLong(16) or 0x00000000FF000000
            Color(colorLong)
        } else {
            fallback
        }
    } catch (_: Throwable) {
        fallback
    }
}
