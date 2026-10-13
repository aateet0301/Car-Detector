package com.example.ui.theme

import androidx.compose.ui.graphics.Color

val CarbonDark = Color(0xFF090D14)
val CarbonSurface = Color(0xFF111A29)
val CarbonCard = Color(0xFF162338)
val CarbonCardBorder = Color(0xFF223654)

val SpeedBlue = Color(0xFF00E5FF)
val SpeedBlueVariant = Color(0xFF00B0FF)
val ElectricAccent = Color(0xFF3D5AFE)
val RacingRed = Color(0xFFFF3D00)
val NeonGreen = Color(0xFF00E676)
val AmberWarning = Color(0xFFFFAB00)

val TextPrimary = Color(0xFFF1F5F9)
val TextSecondary = Color(0xFF94A3B8)
val TextMuted = Color(0xFF64748B)

val DarkColorScheme = androidx.compose.material3.darkColorScheme(
    primary = SpeedBlue,
    onPrimary = Color(0xFF002238),
    primaryContainer = Color(0xFF004975),
    onPrimaryContainer = Color(0xFFCBE6FF),
    secondary = ElectricAccent,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF1C2D5A),
    onSecondaryContainer = Color(0xFFD6E3FF),
    tertiary = RacingRed,
    onTertiary = Color.White,
    background = CarbonDark,
    onBackground = TextPrimary,
    surface = CarbonSurface,
    onSurface = TextPrimary,
    surfaceVariant = CarbonCard,
    onSurfaceVariant = TextSecondary,
    outline = CarbonCardBorder
)

val LightColorScheme = DarkColorScheme // Modern automotive apps prioritize dark cockpit styling
