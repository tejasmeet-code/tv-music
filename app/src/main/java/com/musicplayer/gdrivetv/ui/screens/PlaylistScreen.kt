package com.musicplayer.gdrivetv.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.*
import com.musicplayer.gdrivetv.database.PlaylistEntity
import com.musicplayer.gdrivetv.database.SongEntity
import com.musicplayer.gdrivetv.ui.components.DpadFocusableItem

@Composable
fun PlaylistScreen(
    playlists: List<PlaylistEntity>,
    selectedPlaylistSongs: List<SongEntity>,
    selectedPlaylist: PlaylistEntity?,
    onCreatePlaylist: (String) -> Unit,
    onDeletePlaylist: (PlaylistEntity) -> Unit,
    onSelectPlaylist: (PlaylistEntity) -> Unit,
    onPlaySong: (SongEntity) -> Unit,
    onRemoveSong: (SongEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var playlistNameInput by remember { mutableStateOf("") }

    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Left Column: Playlists List
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(end = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Playlists",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Button(onClick = { showCreateDialog = true }) {
                    Text("Create")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(playlists) { playlist ->
                    DpadFocusableItem(
                        onClick = { onSelectPlaylist(playlist) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        focusedBackgroundColor = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.List,
                                    contentDescription = "Playlist",
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = playlist.name,
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                            }
                            
                            IconButton(onClick = { onDeletePlaylist(playlist) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Playlist",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            }
        }

        // Right Column: Playlist Songs
        Column(
            modifier = Modifier
                .weight(1.5f)
                .fillMaxHeight()
        ) {
            if (selectedPlaylist != null) {
                Text(
                    text = "Songs in '${selectedPlaylist.name}'",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (selectedPlaylistSongs.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No songs in this playlist. Go to library to add.",
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(selectedPlaylistSongs) { song ->
                            DpadFocusableItem(
                                onClick = { onPlaySong(song) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                focusedBackgroundColor = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = song.title,
                                        fontSize = 15.sp,
                                        color = Color.White
                                    )
                                    IconButton(onClick = { onRemoveSong(song) }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Remove song",
                                            tint = Color.LightGray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Select a playlist to view songs",
                        color = Color.Gray,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }

    // Dialog for Creating Playlist (Since TV apps need custom dialog layout, a simple Compose Dialog is used)
    if (showCreateDialog) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { showCreateDialog = false }) {
            androidx.tv.material3.Surface(
                modifier = Modifier
                    .width(400.dp)
                    .wrapContentHeight()
                    .background(Color(0xFF1E1E1E), androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                    .border(1.dp, Color.Gray, androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "New Playlist",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Text Field for TV input (using BasicTextField for zero dependency safety)
                    androidx.compose.foundation.text.BasicTextField(
                        value = playlistNameInput,
                        onValueChange = { playlistNameInput = it },
                        textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 16.sp),
                        cursorBrush = androidx.compose.ui.graphics.SolidColor(Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF2E2E2E), androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                            .border(1.dp, Color.Gray, androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                            .padding(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { showCreateDialog = false },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                if (playlistNameInput.isNotBlank()) {
                                    onCreatePlaylist(playlistNameInput)
                                    playlistNameInput = ""
                                    showCreateDialog = false
                                }
                            }
                        ) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }
}
