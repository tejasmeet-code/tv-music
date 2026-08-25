package com.musicplayer.gdrivetv.sync

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GoogleDriveService {
    @GET("drive/v3/files")
    suspend fun listFiles(
        @Query("q") query: String,
        @Query("fields") fields: String = "files(id, name, mimeType, size)",
        @Query("key") apiKey: String? = null,
        @Header("Authorization") authHeader: String? = null
    ): DriveFilesResponse

    @GET("drive/v3/files/{fileId}")
    suspend fun getFileMetadata(
        @Path("fileId") fileId: String,
        @Query("fields") fields: String = "id, name, mimeType, size",
        @Query("key") apiKey: String? = null,
        @Header("Authorization") authHeader: String? = null
    ): DriveFile
}

data class DriveFilesResponse(
    @SerializedName("files") val files: List<DriveFile>
)

data class DriveFile(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("mimeType") val mimeType: String,
    @SerializedName("size") val size: String? = null,
    var playlistName: String? = null
)

object GDriveHelper {
    private const val BASE_URL = "https://www.googleapis.com/"

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    val service: GoogleDriveService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GoogleDriveService::class.java)

    /**
     * Recursively list all audio files under the specified folder ID.
     */
    suspend fun fetchAllAudioFiles(
        folderId: String,
        apiKey: String?,
        accessToken: String?
    ): List<DriveFile> {
        val result = mutableListOf<DriveFile>()
        val authHeader = accessToken?.let { "Bearer $it" }
        scanFolderRecursive(folderId, null, apiKey, authHeader, result)
        return result
    }

    private suspend fun scanFolderRecursive(
        folderId: String,
        folderName: String?,
        apiKey: String?,
        authHeader: String?,
        outFiles: MutableList<DriveFile>
    ) {
        val q = "'$folderId' in parents and trashed = false"
        try {
            val response = service.listFiles(query = q, apiKey = apiKey, authHeader = authHeader)
            for (file in response.files) {
                if (file.mimeType == "application/vnd.google-apps.folder") {
                    // Recurse into subfolder, passing its name as folderName
                    scanFolderRecursive(file.id, file.name, apiKey, authHeader, outFiles)
                } else if (file.name.endsWith(".mp3", ignoreCase = true) ||
                           file.name.endsWith(".m4a", ignoreCase = true)
                ) {
                    file.playlistName = folderName
                    outFiles.add(file)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Helper to build the download URL for a file.
     */
    fun getDownloadUrl(fileId: String, apiKey: String?): String {
        return if (apiKey != null) {
            "$BASE_URL/drive/v3/files/$fileId?alt=media&key=$apiKey"
        } else {
            "https://docs.google.com/uc?export=download&id=$fileId"
        }
    }
}
