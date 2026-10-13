package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var copiedToClipboard by remember { mutableStateOf(false) }

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

        // Primary Identification Hero Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("car_result_hero_card"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CarbonCard),
            border = BorderStroke(1.dp, SpeedBlue.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Confidence tag & Color pill
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isHighConf = result.confidenceLevel.startsWith("High", ignoreCase = true)
                    val isMedConf = result.confidenceLevel.startsWith("Medium", ignoreCase = true)
                    val confColor = if (isHighConf) NeonGreen else if (isMedConf) AmberWarning else RacingRed

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(confColor.copy(alpha = 0.15f))
                            .border(1.dp, confColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(confColor)
                            )
                            Text(
                                text = "Confidence: ${result.confidenceLevel.substringBefore(" - ").substringBefore(":")}",
                                color = confColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Exterior color pill
                    val heroColor = parseHexColor(result.colorDetails.hexColor, guessColor(result.exteriorColor))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(CarbonDark)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(heroColor)
                                .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                        )
                        Text(
                            text = result.exteriorColor,
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Make & Model
                Column {
                    Text(
                        text = result.makeAndModel,
                        style = MaterialTheme.typography.headlineSmall.copy(
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
                        text = result.bodyStyle,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                HorizontalDivider(color = CarbonCardBorder, thickness = 1.dp)

                // Key Specifications Grid
                Text(
                    text = "Key Specifications",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = TextPrimary
                )

                SpecRowItem(
                    icon = Icons.Default.Engineering,
                    label = "Engine / Powertrain",
                    value = result.enginePowertrain,
                    tint = SpeedBlue
                )

                SpecRowItem(
                    icon = Icons.Default.DirectionsCar,
                    label = "Drivetrain",
                    value = result.drivetrain,
                    tint = Color(0xFF64B5F6)
                )

                SpecRowItem(
                    icon = Icons.Default.Speed,
                    label = "Horsepower",
                    value = result.horsepower,
                    tint = RacingRed
                )

                SpecRowItem(
                    icon = Icons.Default.Star,
                    label = "Notable Features",
                    value = result.notableFeatures,
                    tint = AmberWarning
                )
            }
        }

        // Dedicated Exterior Paint & Color Analysis Card
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
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ColorLens,
                            contentDescription = "Color Details",
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = "Paint & Color Analysis",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF38BDF8).copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "OEM Colorway",
                            color = Color(0xFF38BDF8),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                HorizontalDivider(color = CarbonCardBorder, thickness = 1.dp)

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
                                .size(28.dp)
                                .clip(RoundedCornerShape(bottomEnd = 24.dp))
                                .background(Color.White.copy(alpha = 0.25f))
                        )
                    }

                    // Paint Name & Badges
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = result.colorDetails.primaryColorName.ifEmpty { result.exteriorColor },
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                            color = TextPrimary
                        )

                        // Badges Row: Finish & Code
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Finish Type Badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(CarbonDark)
                                    .border(1.dp, CarbonCardBorder, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FormatPaint,
                                        contentDescription = null,
                                        tint = Color(0xFF38BDF8),
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = result.colorDetails.finishType,
                                        color = TextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            // OEM Paint Code Badge (if available)
                            if (result.colorDetails.factoryPaintCode.isNotBlank() && result.colorDetails.factoryPaintCode != "N/A") {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(CarbonDark)
                                        .border(1.dp, CarbonCardBorder, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Style,
                                            contentDescription = null,
                                            tint = NeonGreen,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Text(
                                            text = "Code: ${result.colorDetails.factoryPaintCode}",
                                            color = TextSecondary,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
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
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "HEX",
                                color = TextMuted,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = result.colorDetails.hexColor,
                                color = TextPrimary,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Color Breakdown Details
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CarbonDark)
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (result.colorDetails.shadeTone.isNotBlank()) {
                        ColorDetailRow(
                            label = "Shade & Undertone",
                            value = result.colorDetails.shadeTone,
                            icon = Icons.Default.AutoAwesome
                        )
                    }

                    if (result.colorDetails.accentDetails.isNotBlank()) {
                        ColorDetailRow(
                            label = "Two-Tone & Accents",
                            value = result.colorDetails.accentDetails,
                            icon = Icons.Default.DirectionsCar
                        )
                    }

                    ColorDetailRow(
                        label = "Finish Characteristic",
                        value = when {
                            result.colorDetails.finishType.contains("Metallic", ignoreCase = true) ->
                                "Multi-stage clearcoat with reflective metallic flakes that shimmer under directional lighting."
                            result.colorDetails.finishType.contains("Pearl", ignoreCase = true) ->
                                "Tri-coat pearlescent luster shifting hues subtly depending on ambient sunlight angle."
                            result.colorDetails.finishType.contains("Matte", ignoreCase = true) || result.colorDetails.finishType.contains("Satin", ignoreCase = true) ->
                                "Low-gloss satin finish designed to diffuse light and accentuate body sculpted lines."
                            else ->
                                "High-gloss solid clearcoat offering deep pure color saturation and mirror-like reflections."
                        },
                        icon = Icons.Default.FormatPaint
                    )
                }
            }
        }

        // Exact AI Structured Markdown Output Card (as specified in prompt)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("exact_ai_formatted_response_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CarbonDark),
            border = BorderStroke(1.dp, CarbonCardBorder)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Official AI Structured Report",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = TextSecondary
                    )
                    OutlinedButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Car Identification", result.toStandardFormattedText())
                            clipboard.setPrimaryClip(clip)
                            copiedToClipboard = true
                            Toast.makeText(context, "Copied formatted report to clipboard!", Toast.LENGTH_SHORT).show()
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
                            text = if (copiedToClipboard) "Copied!" else "Copy",
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
                        text = result.toStandardFormattedText(),
                        modifier = Modifier.padding(14.dp),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        color = TextPrimary,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ColorDetailRow(
    label: String,
    value: String,
    icon: ImageVector
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF38BDF8),
            modifier = Modifier
                .size(16.dp)
                .padding(top = 2.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = TextSecondary,
                fontSize = 11.sp
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                color = TextPrimary,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun SpecRowItem(
    icon: ImageVector,
    label: String,
    value: String,
    tint: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(tint.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tint,
                modifier = Modifier.size(20.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = TextPrimary
            )
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

private fun guessColor(colorName: String): Color {
    val lower = colorName.lowercase()
    return when {
        lower.contains("blue") -> Color(0xFF0077C8)
        lower.contains("red") -> Color(0xFFD81E05)
        lower.contains("white") -> Color(0xFFECEFF1)
        lower.contains("black") -> Color(0xFF1E1E1E)
        lower.contains("green") -> Color(0xFF14452F)
        lower.contains("yellow") -> Color(0xFFFFD600)
        lower.contains("silver") || lower.contains("grey") || lower.contains("gray") -> Color(0xFF9E9E9E)
        lower.contains("orange") -> Color(0xFFFF6D00)
        lower.contains("purple") -> Color(0xFF7C4DFF)
        else -> SpeedBlue
    }
}
