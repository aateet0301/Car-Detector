package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ApiKeyInfo
import com.example.ui.theme.CarbonCard
import com.example.ui.theme.CarbonCardBorder
import com.example.ui.theme.CarbonDark
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.SpeedBlue
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ApiKeyDialog(
    apiKeyInfo: ApiKeyInfo,
    onSaveKey: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var keyInput by remember { mutableStateOf(apiKeyInfo.customKey.ifBlank { "AIzaSyC06wsO_dxSvSi17lY1uZ7swX4q2_ZYZVE" }) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF00375E)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.VpnKey,
                        contentDescription = null,
                        tint = SpeedBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "Gemini API Key",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Key Status Chip
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(CarbonDark)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (apiKeyInfo.hasKey) NeonGreen else Color(0xFFFF5252))
                        )
                        Text(
                            text = if (apiKeyInfo.hasKey) "Status: Connected" else "Status: Key Missing",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = if (apiKeyInfo.hasKey) NeonGreen else Color(0xFFFF5252)
                        )
                    }
                    Text(
                        text = apiKeyInfo.maskedKey,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                }

                Text(
                    text = "Enter your Google Gemini API key to enable live camera and gallery car detection on your Redmi device:",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )

                OutlinedTextField(
                    value = keyInput,
                    onValueChange = { keyInput = it },
                    label = { Text("Paste API Key") },
                    placeholder = { Text("AIzaSy...") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_api_key"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SpeedBlue,
                        unfocusedBorderColor = CarbonCardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedLabelColor = SpeedBlue
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Text(
                    text = "Get a free Gemini API key at aistudio.google.com/apikey",
                    style = MaterialTheme.typography.labelSmall,
                    color = SpeedBlue.copy(alpha = 0.8f)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSaveKey(keyInput)
                    onDismiss()
                },
                modifier = Modifier.testTag("btn_save_api_key"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SpeedBlue,
                    contentColor = Color(0xFF002238)
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Save Key", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            if (apiKeyInfo.isCustom) {
                OutlinedButton(
                    onClick = {
                        onSaveKey("")
                        onDismiss()
                    },
                    modifier = Modifier.testTag("btn_clear_api_key"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF5252)),
                    border = BorderStroke(1.dp, Color(0xFFFF5252).copy(alpha = 0.5f))
                ) {
                    Text("Clear")
                }
            } else {
                TextButton(onClick = onDismiss) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        },
        containerColor = CarbonCard,
        shape = RoundedCornerShape(20.dp)
    )
}
