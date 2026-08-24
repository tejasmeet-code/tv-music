package com.musicplayer.gdrivetv.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.*
import com.musicplayer.gdrivetv.database.SongEntity

@Composable
fun NowPlayingScreen(
    currentSong: SongEntity?,
    isPlaying: Boolean,
    progress: Float,
    playbackTimeStr: String,
    totalDurationStr: String,
    onTogglePlay: () -> Unit,
    onSkipNext: () -> Unit,
    onSkipPrev: () -> Unit,
    isShuffle: Boolean,
    onToggleShuffle: () -> Unit,
    isRepeat: Boolean,
    onToggleRepeat: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E261E),
                        Color(0xFF121212)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            if (currentSong != null) {
                // Vinyl / Cover Art representation
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .clip(RoundedCornerShape(120.dp))
                        .background(Color.DarkGray)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(100.dp))
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Music Playing",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Song Title & Artist
                Text(
                    text = currentSong.title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = currentSong.artist,
                    fontSize = 18.sp,
                    color = Color.LightGray
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Seekbar & Progress Timers
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.width(600.dp)
                ) {
                    Text(
                        text = playbackTimeStr,
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )

                    // Seek slider
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                            .height(6.dp)
                            .background(Color.Gray, RoundedCornerShape(3.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progress)
                                .fillMaxHeight()
                                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(3.dp))
                        )
                    }

                    Text(
                        text = totalDurationStr,
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // TV-Optimized Playback Control Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Shuffle
                    IconButton(onClick = onToggleShuffle) {
                        Icon(
                            imageVector = Icons.Default.Refresh, // Placeholder for Shuffle
                            contentDescription = "Shuffle",
                            tint = if (isShuffle) MaterialTheme.colorScheme.primary else Color.White
                        )
                    }

                    // Skip Previous
                    IconButton(onClick = onSkipPrev) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack, // Placeholder for Prev
                            contentDescription = "Previous",
                            tint = Color.White
                        )
                    }

                    // Play/Pause
                    IconButton(
                        onClick = onTogglePlay,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.PlayArrow else Icons.Default.PlayArrow, // Replace with standard play/pause
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    // Skip Next
                    IconButton(onClick = onSkipNext) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward, // Placeholder for Next
                            contentDescription = "Next",
                            tint = Color.White
                        )
                    }

                    // Repeat
                    IconButton(onClick = onToggleRepeat) {
                        Icon(
                            imageVector = Icons.Default.Share, // Placeholder for Repeat
                            contentDescription = "Repeat",
                            tint = if (isRepeat) MaterialTheme.colorScheme.primary else Color.White
                        )
                    }
                }
            } else {
                Text(
                    text = "No track playing",
                    fontSize = 22.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
