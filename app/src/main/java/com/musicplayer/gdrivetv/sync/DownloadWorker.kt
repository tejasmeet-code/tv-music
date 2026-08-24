package com.musicplayer.gdrivetv.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.musicplayer.gdrivetv.database.AppDatabase
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class DownloadWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val songId = inputData.getString("song_id") ?: return Result.failure()
        val db = AppDatabase.getDatabase(applicationContext)
        val songDao = db.songDao()

        val song = songDao.getSongById(songId) ?: return Result.failure()
        
        // Skip if already downloaded
        if (song.isDownloaded && song.localFilePath != null && File(song.localFilePath).exists()) {
            return Result.success()
        }

        val sharedPrefs = applicationContext.getSharedPreferences("gdrive_music_prefs", Context.MODE_PRIVATE)
        val accessToken = sharedPrefs.getString("access_token", null)

        val client = OkHttpClient()
        val requestBuilder = Request.Builder().url(song.gdriveUrl)
        if (accessToken != null) {
            requestBuilder.addHeader("Authorization", "Bearer $accessToken")
        }

        try {
            val response = client.newCall(requestBuilder.build()).execute()
            if (!response.isSuccessful) {
                return Result.retry()
            }

            val body = response.body ?: return Result.failure()
            val musicDir = File(applicationContext.filesDir, "cached_songs")
            if (!musicDir.exists()) {
                musicDir.mkdirs()
            }

            // Save using GDrive file ID to avoid filename collisions
            val localFile = File(musicDir, "$songId.mp3")
            
            var inputStream: InputStream? = null
            var outputStream: FileOutputStream? = null

            try {
                inputStream = body.byteStream()
                outputStream = FileOutputStream(localFile)
                val data = ByteArray(4096)
                var count: Int
                while (inputStream.read(data).also { count = it } != -1) {
                    outputStream.write(data, 0, count)
                }
                outputStream.flush()

                // Update database
                songDao.updateDownloadStatus(songId, localFile.absolutePath, true)
                return Result.success()
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.retry()
        }
    }
}
