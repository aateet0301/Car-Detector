package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.model.CarDataRepository
import com.example.model.CarIdentificationResult
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CarbonCard
import com.example.ui.theme.CarbonCardBorder
import com.example.ui.theme.CarbonDark
import com.example.ui.theme.CarbonSurface
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.RacingRed
import com.example.ui.theme.SpeedBlue
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun CarResultView(
    result: CarIdentificationResult,
    modifier: Modifier = Modifier,
    scannedUri: Uri? = null,
    scannedBitmap: Bitmap? = null,
    onOverrideModel: ((String) -> Unit)? = null,
    onOpenApiKeyDialog: (() -> Unit)? = null,
    onDismissApiWarning: (() -> Unit)? = null,
    onReset: (() -> Unit)? = null
) {
    val context = LocalContext.current
    var copiedToClipboard by remember { mutableStateOf(false) }
    var showModelPickerDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (!result.isCarDetected) {
            // Unidentified / No car detected state
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("result_no_car_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CarbonCard),
                border = BorderStroke(1.dp, RacingRed.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(RacingRed.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "No car detected",
                            tint = RacingRed,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Text(
                        text = "Vehicle Not Detected",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Text(
                        text = result.messageIfNotDetected
                            ?: "The image does not contain a clear view of a car. Please try uploading a clearer photo of a vehicle.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }
            return
        }

        // [0] API Notice Banner (If key leaked or custom key suggested)
        if (!result.apiWarning.isNullOrBlank()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("api_warning_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF26190B)),
                border = BorderStroke(1.dp, AmberWarning.copy(alpha = 0.6f))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "API Notice",
                            tint = AmberWarning,
                            modifier = Modifier.size(20.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Gemini API Notice",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = AmberWarning
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = result.apiWarning,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextPrimary,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        }
                        if (onDismissApiWarning != null) {
                            IconButton(
                                onClick = onDismissApiWarning,
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Dismiss",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    if (onOpenApiKeyDialog != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = onOpenApiKeyDialog,
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AmberWarning,
                                    contentColor = Color(0xFF26190B)
                                ),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VpnKey,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Set API Key", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        // [1] PROMINENT SCANNED CAR IMAGE CARD
        val hasScannedImage = scannedBitmap != null || scannedUri != null
        if (hasScannedImage) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 10f)
                    .testTag("scanned_car_image_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CarbonCard),
                border = BorderStroke(1.5.dp, SpeedBlue.copy(alpha = 0.5f))
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    when {
                        scannedBitmap != null -> {
                            Image(
                                bitmap = scannedBitmap.asImageBitmap(),
                                contentDescription = "Scanned Car Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        scannedUri != null -> {
                            AsyncImage(
                                model = scannedUri,
                                contentDescription = "Scanned Car Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    // Scanned Image Overlay Badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black.copy(alpha = 0.75f))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null,
                                tint = SpeedBlue,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Scanned Vehicle Photo",
                                color = TextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Color Swatch Tag Overlay
                    val swatchColor = parseHexColor(result.colorDetails.hexColor, guessColor(result.exteriorColor))
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.Black.copy(alpha = 0.8f))
                            .border(1.dp, CarbonCardBorder, RoundedCornerShape(10.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(swatchColor)
                                    .border(1.dp, Color.White.copy(alpha = 0.6f), CircleShape)
                            )
                            Text(
                                text = result.colorDetails.primaryColorName.ifEmpty { result.exteriorColor },
                                color = TextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // [2] MODEL VERIFICATION & QUICK SWITCH STRIP
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("model_quick_switch_card"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = CarbonDark),
            border = BorderStroke(1.dp, SpeedBlue.copy(alpha = 0.35f))
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = SpeedBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Model Verification & Quick Switch",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                    }

                    TextButton(
                        onClick = { showModelPickerDialog = true },
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("Search All Cars", color = SpeedBlue, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                // Quick Switch Chips Row: Maruti 800 is #1!
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val quickModels = listOf(
                        "Maruti Suzuki 800",
                        "Maruti Suzuki Swift",
                        "Maruti Suzuki Alto 800",
                        "Maruti Suzuki WagonR",
                        "Hyundai Grand i10 Nios",
                        "Tata Nexon",
                        "Mahindra Thar",
                        "Honda City",
                        "Toyota Innova Crysta",
                        "Hyundai Creta",
                        "Toyota Fortuner",
                        "Suzuki Jimny",
                        "Audi RS6 Avant",
                        "Audi A4",
                        "Porsche 911 Carrera (992)"
                    )

                    quickModels.forEach { modelName ->
                        val isSelected = result.makeAndModel.contains(modelName, ignoreCase = true) ||
                                modelName.contains(result.makeAndModel, ignoreCase = true)
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) SpeedBlue else CarbonCard,
                            border = BorderStroke(1.dp, if (isSelected) SpeedBlue else CarbonCardBorder),
                            modifier = Modifier.clickable {
                                if (onOverrideModel != null && !isSelected) {
                                    onOverrideModel(modelName)
                                    Toast.makeText(context, "Switched model to $modelName", Toast.LENGTH_SHORT).show()
                                }
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = if (modelName.contains("800")) "🚗 $modelName" else modelName,
                                    color = if (isSelected) Color(0xFF002238) else TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }

        // [3] NEAT & SYSTEMATIC SPECIFICATION REPORT: Hero Identity Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("car_result_hero_card"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = CarbonCard),
            border = BorderStroke(1.5.dp, SpeedBlue.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Systematic Header Strip with AutoLens AI Logo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_cars360_logo),
                            contentDescription = null,
                            modifier = Modifier.size(22.dp)
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SpeedBlue.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "CARS 360 SPECIFICATION REPORT",
                                color = SpeedBlue,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                        }
                    }

                    // Confidence Status
                    val isHighConf = result.confidenceLevel.startsWith("High", ignoreCase = true)
                    val confColor = if (isHighConf) NeonGreen else AmberWarning
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(confColor)
                        )
                        Text(
                            text = if (result.isFallback) "Pattern Verified" else "AI Vision Verified",
                            color = confColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Make & Model Primary Headline
                Column {
                    Text(
                        text = result.makeAndModel,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.2.sp
                        ),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = result.estimatedYearGen,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = SpeedBlue
                    )
                    Text(
                        text = "${result.vehicleSegment} • ${result.bodyStyle}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                HorizontalDivider(color = CarbonCardBorder, thickness = 1.dp)

                // Systematic Identification Table
                SystematicTableHeader(
                    icon = Icons.Default.DirectionsCar,
                    title = "1. VEHICLE IDENTITY & CLASSIFICATION"
                )
                SystematicFormGrid(
                    rows = listOf(
                        "Manufacturer / Make" to (result.brandMake.ifEmpty { result.makeAndModel.substringBefore(" ") }),
                        "Model Designation" to (result.modelName.ifEmpty { result.makeAndModel }),
                        "Generation / Era" to result.estimatedYearGen,
                        "Vehicle Segment" to result.vehicleSegment,
                        "Body Configuration" to result.bodyStyle
                    )
                )
            }
        }

        // [4] SYSTEMATIC SPECIFICATION REPORT: Exterior Paint & Color Analysis Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("car_result_color_card"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = CarbonCard),
            border = BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                SystematicTableHeader(
                    icon = Icons.Default.ColorLens,
                    title = "2. EXTERIOR COLOR & FACTORY PAINT SPECIFICATION"
                )

                // Visual Swatch and Paint Name Hero
                val swatchColor = parseHexColor(result.colorDetails.hexColor, guessColor(result.exteriorColor))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Big Color Swatch Circle with Gloss Reflection
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        swatchColor,
                                        swatchColor.copy(alpha = 0.85f),
                                        swatchColor
                                    )
                                )
                            )
                            .border(2.5.dp, Color.White.copy(alpha = 0.4f), CircleShape)
                    ) {
                        // Gloss Reflection Overlay Arc
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(RoundedCornerShape(bottomEnd = 24.dp))
                                .background(Color.White.copy(alpha = 0.25f))
                        )
                    }

                    // Paint Name & Badges
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = result.colorDetails.primaryColorName.ifEmpty { result.exteriorColor },
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                            color = TextPrimary
                        )

                        Text(
                            text = "Finish: ${result.colorDetails.finishType}",
                            color = Color(0xFF38BDF8),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Hex Code Pill (Tap to Copy)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(CarbonDark)
                            .border(1.dp, CarbonCardBorder, RoundedCornerShape(8.dp))
                            .clickable {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("Color Hex", result.colorDetails.hexColor))
                                Toast.makeText(context, "Copied Hex ${result.colorDetails.hexColor}", Toast.LENGTH_SHORT).show()
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "HEX CODE", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text(text = result.colorDetails.hexColor, color = SpeedBlue, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                HorizontalDivider(color = CarbonCardBorder, thickness = 1.dp)

                // Systematic Paint Spec Grid
                SystematicFormGrid(
                    rows = listOf(
                        "Primary Colorway" to result.colorDetails.primaryColorName.ifEmpty { result.exteriorColor },
                        "Paint Finish Classification" to result.colorDetails.finishType,
                        "Factory Paint Code" to result.colorDetails.factoryPaintCode,
                        "Shade, Undertone & Luster" to result.colorDetails.shadeTone,
                        "Accent & Trim Styling" to result.colorDetails.accentDetails
                    )
                )
            }
        }

        // [5] SYSTEMATIC SPECIFICATION REPORT: Powertrain & Mechanical Architecture Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("car_result_powertrain_card"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = CarbonCard),
            border = BorderStroke(1.dp, NeonGreen.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                SystematicTableHeader(
                    icon = Icons.Default.Engineering,
                    title = "3. POWERTRAIN & MECHANICAL ARCHITECTURE"
                )

                SystematicFormGrid(
                    rows = listOf(
                        "Engine Powertrain" to result.enginePowertrain,
                        "Maximum Horsepower" to result.horsepower,
                        "Peak Torque" to (if (result.torque.isNotBlank() && result.torque != "N/A") result.torque else "Factory Rated"),
                        "Drivetrain Layout" to result.drivetrain,
                        "Transmission" to result.transmission,
                        "Fuel / Propulsion Type" to result.fuelType,
                        "Top Speed & Mileage" to (if (result.topSpeedMileage.isNotBlank() && result.topSpeedMileage != "N/A") result.topSpeedMileage else "Factory Rating")
                    )
                )
            }
        }

        // [6] SYSTEMATIC SPECIFICATION REPORT: Chassis, Dimensions & Capacities Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("car_result_chassis_card"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = CarbonCard),
            border = BorderStroke(1.dp, SpeedBlue.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SystematicTableHeader(
                    icon = Icons.Default.SquareFoot,
                    title = "4. CHASSIS, DIMENSIONS & CAPACITIES"
                )

                Surface(
                    color = CarbonDark,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, CarbonCardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Dimensions & Structure",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = SpeedBlue
                        )
                        Text(
                            text = result.dimensionsWeight,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }

        // [7] SYSTEMATIC SPECIFICATION REPORT: Design Profile & Key Identifiers
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("car_result_design_card"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = CarbonCard),
            border = BorderStroke(1.dp, AmberWarning.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SystematicTableHeader(
                    icon = Icons.Default.Star,
                    title = "5. DESIGN PROFILE & KEY IDENTIFIERS"
                )

                Surface(
                    color = CarbonDark,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, CarbonCardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Distinctive Visual Markers & Silhouette",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = TextSecondary
                        )
                        Text(
                            text = result.notableFeatures,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }

        // [8] ACTION TOOLBAR: Copy Spec Sheet, Scan Another
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("exact_ai_formatted_response_card"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = CarbonDark),
            border = BorderStroke(1.dp, CarbonCardBorder)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Systematic Spec Sheet Actions",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextSecondary
                    )

                    OutlinedButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Car Identification", result.toSystematicReport())
                            clipboard.setPrimaryClip(clip)
                            copiedToClipboard = true
                            Toast.makeText(context, "Copied systematic spec sheet to clipboard!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.testTag("btn_copy_report"),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        border = BorderStroke(1.dp, SpeedBlue.copy(alpha = 0.5f))
                    ) {
                        Icon(
                            imageVector = if (copiedToClipboard) Icons.Default.CheckCircle else Icons.Default.ContentCopy,
                            contentDescription = "Copy Report",
                            tint = SpeedBlue,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (copiedToClipboard) "Copied!" else "Copy Spec Sheet",
                            color = SpeedBlue,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Surface(
                    color = CarbonSurface,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, CarbonCardBorder)
                ) {
                    Text(
                        text = result.toSystematicReport(),
                        modifier = Modifier.padding(14.dp),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        color = TextPrimary,
                        lineHeight = 17.sp
                    )
                }
            }
        }
    }

    // Full Vehicle Model Picker Modal
    if (showModelPickerDialog) {
        var searchQuery by remember { mutableStateOf("") }
        val allProfiles = CarDataRepository.vehicleCatalog
        val filteredProfiles = remember(searchQuery) {
            if (searchQuery.isBlank()) allProfiles
            else allProfiles.filter {
                it.makeAndModel.contains(searchQuery, ignoreCase = true) ||
                it.brandMake.contains(searchQuery, ignoreCase = true) ||
                it.vehicleSegment.contains(searchQuery, ignoreCase = true)
            }
        }

        AlertDialog(
            onDismissRequest = { showModelPickerDialog = false },
            title = {
                Text(
                    text = "Select Correct Vehicle Model",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Search make or model (e.g. Maruti 800)") },
                        placeholder = { Text("Maruti 800, Swift, Thar...") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = SpeedBlue)
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SpeedBlue,
                            unfocusedBorderColor = CarbonCardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredProfiles) { profile ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = CarbonDark,
                                border = BorderStroke(1.dp, CarbonCardBorder),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (onOverrideModel != null) {
                                            onOverrideModel(profile.makeAndModel)
                                            Toast.makeText(context, "Selected ${profile.makeAndModel}", Toast.LENGTH_SHORT).show()
                                        }
                                        showModelPickerDialog = false
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = profile.makeAndModel,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = "${profile.vehicleSegment} • ${profile.enginePowertrain}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextSecondary
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.DirectionsCar,
                                        contentDescription = null,
                                        tint = SpeedBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showModelPickerDialog = false }) {
                    Text("Close", color = SpeedBlue)
                }
            },
            containerColor = CarbonCard
        )
    }
}

@Composable
private fun SystematicTableHeader(
    title: String,
    icon: ImageVector? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SpeedBlue,
                modifier = Modifier.size(15.dp)
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = SpeedBlue,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
private fun SystematicFormGrid(rows: List<Pair<String, String>>) {
    Surface(
        color = CarbonDark,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, CarbonCardBorder)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            rows.forEachIndexed { index, (label, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        modifier = Modifier.weight(1.1f)
                    )
                    Text(
                        text = value.ifEmpty { "N/A" },
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = TextPrimary,
                        modifier = Modifier.weight(1.4f)
                    )
                }
                if (index < rows.size - 1) {
                    HorizontalDivider(color = CarbonCardBorder.copy(alpha = 0.5f), thickness = 0.5.dp)
                }
            }
        }
    }
}

private fun guessColor(name: String): Color {
    val lower = name.lowercase()
    return when {
        lower.contains("red") -> Color(0xFFC62828)
        lower.contains("blue") -> Color(0xFF1565C0)
        lower.contains("green") -> Color(0xFF2E7D32)
        lower.contains("yellow") || lower.contains("gold") -> Color(0xFFF9A825)
        lower.contains("orange") -> Color(0xFFEF6C00)
        lower.contains("purple") -> Color(0xFF6A1B9A)
        lower.contains("white") || lower.contains("polar") || lower.contains("superior") -> Color(0xFFF8F9FA)
        lower.contains("silver") || lower.contains("silky") -> Color(0xFFCFD8DC)
        lower.contains("gray") || lower.contains("grey") || lower.contains("nardo") -> Color(0xFF78909C)
        lower.contains("black") -> Color(0xFF212121)
        else -> Color(0xFFC62828)
    }
}

private fun parseHexColor(hexString: String, fallback: Color): Color {
    val clean = hexString.trim().removePrefix("#")
    if (clean.length == 6) {
        return try {
            val colorInt = clean.toLong(16) or 0x00000000FF000000L
            Color(colorInt)
        } catch (_: Exception) {
            fallback
        }
    } else if (clean.length == 8) {
        return try {
            val colorInt = clean.toLong(16)
            Color(colorInt)
        } catch (_: Exception) {
            fallback
        }
    }
    return fallback
}
