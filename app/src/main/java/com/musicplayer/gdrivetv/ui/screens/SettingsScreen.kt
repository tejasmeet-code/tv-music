package com.musicplayer.gdrivetv.ui.screens

import android.text.format.Formatter
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.*
import com.musicplayer.gdrivetv.ui.components.DpadFocusableItem

@Composable
fun SettingsScreen(
    onTriggerManualSync: () -> Unit,
    folderId: String,
    onFolderIdChanged: (String) -> Unit,
    apiKey: String,
    onApiKeyChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var folderInput by remember { mutableStateOf(folderId) }
    var apiInput by remember { mutableStateOf(apiKey) }

    // Memory stats helper for Mi TV optimization
    val runtime = Runtime.getRuntime()
    val maxMemory = runtime.maxMemory()
    val allocatedMemory = runtime.totalMemory()
    val freeMemory = runtime.freeMemory()
    val usedMemory = allocatedMemory - freeMemory
    
    val maxMemoryStr = Formatter.formatShortFileSize(context, maxMemory)
    val usedMemoryStr = Formatter.formatShortFileSize(context, usedMemory)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Settings & Sync Configuration",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1.2f)) {
                // Folder ID Config
                Text("Google Drive Folder ID", color = Color.White, fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp))
                androidx.compose.foundation.text.BasicTextField(
                    value = folderInput,
                    onValueChange = {
                        folderInput = it
                        onFolderIdChanged(it)
                    },
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 15.sp),
                    cursorBrush = androidx.compose.ui.graphics.SolidColor(Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2E2E2E), androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                        .border(1.dp, Color.Gray, androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                        .padding(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // API Key Config
                Text("Google Drive API Key (Optional)", color = Color.White, fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp))
                androidx.compose.foundation.text.BasicTextField(
                    value = apiInput,
                    onValueChange = {
                        apiInput = it
                        onApiKeyChanged(it)
                    },
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 15.sp),
                    cursorBrush = androidx.compose.ui.graphics.SolidColor(Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2E2E2E), androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                        .border(1.dp, Color.Gray, androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                        .padding(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onTriggerManualSync,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Trigger Library Sync Now")
                }
            }

            Spacer(modifier = Modifier.width(32.dp))

            // Right side: Optimization stats (crucial for Mi 4x Low-RAM Tv)
            Column(modifier = Modifier.weight(0.8f)) {
                Text(
                    text = "System Information",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Target Folder ID: \n$folderId",
                    fontSize = 14.sp,
                    color = Color.LightGray
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Device RAM Stats (App Heap):",
                    fontSize = 14.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Used Heap: $usedMemoryStr / Max Allowed: $maxMemoryStr",
                    fontSize = 13.sp,
                    color = Color.Green
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Mi TV 4x Optimization Status:",
                    fontSize = 14.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "• Coil RAM cache limited\n• Transitions minimized\n• Audio buffers managed\n• Sticky Service running",
                    fontSize = 13.sp,
                    color = Color.LightGray
                )
            }
        }
    }
}
