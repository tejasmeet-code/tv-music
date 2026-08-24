package com.musicplayer.gdrivetv.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.*
import com.musicplayer.gdrivetv.database.SongEntity
import com.musicplayer.gdrivetv.ui.components.DpadFocusableItem

@Composable
fun LibraryScreen(
    songs: List<SongEntity>,
    onPlaySong: (SongEntity) -> Unit,
    onAddSongToPlaylist: (SongEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Library Sync - Google Drive",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (songs.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No songs synced yet. Connecting to Google Drive...",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3), // 3 columns for landscape TV screens
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(songs) { song ->
                    SongGridItem(
                        song = song,
                        onPlay = { onPlaySong(song) },
                        onAddToPlaylist = { onAddSongToPlaylist(song) }
                    )
                }
            }
        }
    }
}

@Composable
fun SongGridItem(
    song: SongEntity,
    onPlay: () -> Unit,
    onAddToPlaylist: () -> Unit
) {
    DpadFocusableItem(
        onClick = onPlay,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        focusedBackgroundColor = MaterialTheme.colorScheme.surfaceVariant
    ) { isFocused ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon / Art Placeholder
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = if (isFocused) MaterialTheme.colorScheme.primary else Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1fr)
            ) {
                Text(
                    text = song.title,
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = song.artist,
                    fontSize = 14.sp,
                    color = Color.LightGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (song.isDownloaded) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Offline Available",
                            tint = Color.Green,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Offline",
                            fontSize = 11.sp,
                            color = Color.Green
                        )
                    } else {
                        Text(
                            text = "Streamable",
                            fontSize = 11.sp,
                            color = Color.LightGray
                        )
                    }
                }
            }

            // Playlist quick action (only shown or focused when interacting)
            IconButton(
                onClick = onAddToPlaylist,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add to playlist",
                    tint = Color.White
                )
            }
        }
    }
}
