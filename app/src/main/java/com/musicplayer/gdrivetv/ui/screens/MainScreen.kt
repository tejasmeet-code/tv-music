package com.musicplayer.gdrivetv.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.*
import com.musicplayer.gdrivetv.ui.components.DpadFocusableItem

sealed class TvScreen(val name: String) {
    object Library : TvScreen("Library")
    object Playlists : TvScreen("Playlists")
    object NowPlaying : TvScreen("Now Playing")
    object Settings : TvScreen("Settings")
}

@Composable
fun MainScreen(
    currentScreen: TvScreen,
    onNavigate: (TvScreen) -> Unit,
    content: @Composable () -> Unit
) {
    Row(modifier = Modifier.fillMaxSize()) {
        // Navigation sidebar
        Column(
            modifier = Modifier
                .width(200.dp)
                .fillMaxHeight()
                .background(Color(0xFF161616))
                .padding(vertical = 24.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "GDrive Music",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 12.dp, bottom = 32.dp)
            )

            val menuItems = listOf(
                TvScreen.Library to Icons.Default.Home,
                TvScreen.Playlists to Icons.Default.List,
                TvScreen.NowPlaying to Icons.Default.PlayArrow,
                TvScreen.Settings to Icons.Default.Settings
            )

            menuItems.forEach { (screen, icon) ->
                DpadFocusableItem(
                    onClick = { onNavigate(screen) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .height(48.dp),
                    focusedBackgroundColor = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = screen.name,
                            tint = if (currentScreen == screen) MaterialTheme.colorScheme.primary else Color.LightGray
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = screen.name,
                            fontSize = 15.sp,
                            color = if (currentScreen == screen) Color.White else Color.LightGray
                        )
                    }
                }
            }
        }

        // Active content screen
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Color(0xFF101010))
        ) {
            content()
        }
    }
}
