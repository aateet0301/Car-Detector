package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.ApiKeyInfo
import com.example.ui.theme.CarbonCard
import com.example.ui.theme.CarbonCardBorder
import com.example.ui.theme.CarbonDark
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.RacingRed
import com.example.ui.theme.SpeedBlue
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun ApiKeyDialog(
    apiKeyInfo: ApiKeyInfo,
    onSaveKey: (String) -> Unit,
    onDismiss: () -> Unit,
    onTestKey: (suspend (String) -> Pair<Boolean, String>)? = null
) {
    var keyInput by remember { mutableStateOf(apiKeyInfo.customKey) }
    var isTesting by remember { mutableStateOf(false) }
    var testResult by remember { mutableStateOf<Pair<Boolean, String>?>(null) }
    val scope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF071B2C)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_cars360_logo),
                        contentDescription = "AutoLens AI",
                        modifier = Modifier.size(30.dp)
                    )
                }
                Column {
                    Text(
                        text = "Automotive AI Vision Key",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Text(
                        text = "Universal Multimodal Vision (All Key Formats)",
                        style = MaterialTheme.typography.labelSmall,
                        color = SpeedBlue
                    )
                }
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
                                .background(if (apiKeyInfo.hasKey) NeonGreen else Color(0xFFFFB300))
                        )
                        Text(
                            text = if (apiKeyInfo.hasKey) "Active API Key Configured" else "Using Smart On-Device Mode",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = if (apiKeyInfo.hasKey) NeonGreen else Color(0xFFFFB300)
                        )
                    }
                    Text(
                        text = apiKeyInfo.maskedKey,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                }

                Text(
                    text = "Paste your API key below for vehicle scanning and specifications. All key types are supported (Google Gemini, OpenAI sk-..., Groq gsk_..., or any custom provider):",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )

                // Input field + Paste button row
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedTextField(
                        value = keyInput,
                        onValueChange = {
                            keyInput = it
                            testResult = null
                        },
                        label = { Text("API Key") },
                        placeholder = { Text("Paste your API key here...") },
                        singleLine = true,
                        trailingIcon = {
                            TextButton(
                                onClick = {
                                    val clipText = clipboardManager.getText()?.text?.trim()
                                    if (!clipText.isNullOrBlank()) {
                                        keyInput = clipText
                                        testResult = null
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentPaste,
                                    contentDescription = "Paste",
                                    tint = SpeedBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Paste", color = SpeedBlue, fontSize = 12.sp)
                            }
                        },
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
                }

                // Test Key Action Row
                if (onTestKey != null && keyInput.isNotBlank()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                scope.launch {
                                    isTesting = true
                                    testResult = onTestKey(keyInput.trim())
                                    isTesting = false
                                }
                            },
                            enabled = !isTesting,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, SpeedBlue.copy(alpha = 0.6f))
                        ) {
                            if (isTesting) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = SpeedBlue
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Testing...", fontSize = 12.sp)
                            } else {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = SpeedBlue
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Test Key Now", fontSize = 12.sp, color = SpeedBlue)
                            }
                        }

                        if (testResult != null) {
                            val (isValid, _) = testResult!!
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier
                                    .weight(1f, fill = false)
                                    .padding(start = 8.dp)
                            ) {
                                Icon(
                                    imageVector = if (isValid) Icons.Default.CheckCircle else Icons.Default.Error,
                                    contentDescription = null,
                                    tint = if (isValid) NeonGreen else RacingRed,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = if (isValid) "Connected" else "Check Key",
                                    color = if (isValid) NeonGreen else RacingRed,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    if (testResult != null) {
                        Text(
                            text = testResult!!.second,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (testResult!!.first) NeonGreen else RacingRed,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                }

                Text(
                    text = "Key is saved locally on device. Tap 'Save Key' to start identifying cars.",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSaveKey(keyInput.trim())
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
