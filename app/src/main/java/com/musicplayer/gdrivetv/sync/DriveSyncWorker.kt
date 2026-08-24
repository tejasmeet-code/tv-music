package com.musicplayer.gdrivetv.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.musicplayer.gdrivetv.database.AppDatabase
import com.musicplayer.gdrivetv.database.SongEntity

class DriveSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val sharedPrefs = applicationContext.getSharedPreferences("gdrive_music_prefs", Context.MODE_PRIVATE)
        val folderId = sharedPrefs.getString("folder_id", "1-mRwu6GyYVIX_AJYI2dP-HtY6Hoip3Zd") ?: "1-mRwu6GyYVIX_AJYI2dP-HtY6Hoip3Zd"
        val apiKey = sharedPrefs.getString("api_key", "AIzaSyDao07CoaqI09Mo9zsR2NzmGoH8XBaCqMY")
        val accessToken = sharedPrefs.getString("access_token", null)

        val db = AppDatabase.getDatabase(applicationContext)
        val songDao = db.songDao()

        try {
            // 1. Fetch remote files
            val remoteFiles = GDriveHelper.fetchAllAudioFiles(folderId, apiKey, accessToken)
            if (remoteFiles.isEmpty()) {
                return Result.success()
            }

            // 2. Load local files
            val localSongs = songDao.getAllSongsList()
            val localSongsMap = localSongs.associateBy { it.id }

            val remoteIds = remoteFiles.map { it.id }.toSet()

            // 3. Find files to delete (present locally but not in remote)
            val songsToDelete = localSongs.filter { it.id !in remoteIds }
            for (song in songsToDelete) {
                // Delete local file
                song.localFilePath?.let { path ->
                    val file = java.io.File(path)
                    if (file.exists()) {
                        file.delete()
                    }
                }
                songDao.deleteSong(song)
            }

            // 4. Find new files and insert/update
            val songsToInsert = mutableListOf<SongEntity>()
            for (remoteFile in remoteFiles) {
                val existing = localSongsMap[remoteFile.id]
                if (existing == null) {
                    val downloadUrl = GDriveHelper.getDownloadUrl(remoteFile.id, apiKey)
                    val cleanTitle = remoteFile.name.substringBeforeLast(".")
                    val newSong = SongEntity(
                        id = remoteFile.id,
                        title = cleanTitle,
                        gdriveUrl = downloadUrl,
                        sizeBytes = remoteFile.size?.toLongOrNull() ?: 0L,
                        isDownloaded = false
                    )
                    songsToInsert.add(newSong)
                }
            }

            if (songsToInsert.isNotEmpty()) {
                songDao.insertSongs(songsToInsert)
            }

            // 5. Trigger Download Worker for non-downloaded songs
            val songsToDownload = songDao.getAllSongsList().filter { !it.isDownloaded }
            val workManager = WorkManager.getInstance(applicationContext)
            
            for (song in songsToDownload) {
                val downloadRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
                    .setInputData(workDataOf("song_id" to song.id))
                    .build()
                workManager.enqueue(downloadRequest)
            }

            return Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.retry()
        }
    }
}
