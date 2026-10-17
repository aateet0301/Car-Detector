package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Garage
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.AppScreen
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CarbonCard
import com.example.ui.theme.CarbonCardBorder
import com.example.ui.theme.CarbonSurface
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.SpeedBlue
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AutomotiveTopBar(
    historyCount: Int,
    hasApiKey: Boolean,
    onOpenApiKeyDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = CarbonSurface,
        tonalElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Branded Logo & App Name "Cars 360"
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF071B2C))
                            .border(1.dp, SpeedBlue.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_cars360_logo),
                            contentDescription = "Cars 360 Logo",
                            modifier = Modifier.size(34.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Cars 360",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.5.sp
                            ),
                            color = TextPrimary
                        )
                        Text(
                            text = "Vehicle Scanner & Specs",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                // API Key / Vision Settings Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(CarbonCard)
                        .border(
                            1.dp,
                            if (hasApiKey) SpeedBlue.copy(alpha = 0.4f) else AmberWarning.copy(alpha = 0.6f),
                            RoundedCornerShape(20.dp)
                        )
                        .clickable(onClick = onOpenApiKeyDialog)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("btn_topbar_api_key")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(if (hasApiKey) NeonGreen else AmberWarning)
                        )
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = null,
                            tint = if (hasApiKey) SpeedBlue else AmberWarning,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = if (hasApiKey) "API Key" else "Add Key",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = if (hasApiKey) TextPrimary else AmberWarning
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AutomotiveBottomNav(
    currentScreen: AppScreen,
    historyCount: Int,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.navigationBarsPadding(),
        containerColor = CarbonSurface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            modifier = Modifier.testTag("nav_scanner"),
            selected = currentScreen == AppScreen.SCANNER,
            onClick = { onNavigate(AppScreen.SCANNER) },
            icon = {
                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = "Scanner",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = "Scanner",
                    fontWeight = if (currentScreen == AppScreen.SCANNER) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SpeedBlue,
                selectedTextColor = SpeedBlue,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted,
                indicatorColor = CarbonCard
            )
        )

        NavigationBarItem(
            modifier = Modifier.testTag("nav_garage"),
            selected = currentScreen == AppScreen.GARAGE,
            onClick = { onNavigate(AppScreen.GARAGE) },
            icon = {
                if (historyCount > 0) {
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = SpeedBlue,
                                contentColor = Color(0xFF002238)
                            ) {
                                Text(
                                    text = historyCount.toString(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Garage,
                            contentDescription = "My Garage",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.Garage,
                        contentDescription = "My Garage",
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            label = {
                Text(
                    text = "My Garage",
                    fontWeight = if (currentScreen == AppScreen.GARAGE) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SpeedBlue,
                selectedTextColor = SpeedBlue,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted,
                indicatorColor = CarbonCard
            )
        )
    }
}
