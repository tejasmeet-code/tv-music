package com.musicplayer.gdrivetv.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey val id: String, // Google Drive File ID
    val title: String,
    val artist: String = "Unknown Artist",
    val duration: Long = 0L, // In milliseconds
    val localFilePath: String? = null,
    val gdriveUrl: String,
    val sizeBytes: Long = 0L,
    val isDownloaded: Boolean = false,
    val dateSynced: Long = System.currentTimeMillis()
)
