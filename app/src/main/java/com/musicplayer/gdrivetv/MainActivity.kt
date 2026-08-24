package com.musicplayer.gdrivetv

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.musicplayer.gdrivetv.database.AppDatabase
import com.musicplayer.gdrivetv.database.PlaylistEntity
import com.musicplayer.gdrivetv.database.PlaylistSongCrossRef
import com.musicplayer.gdrivetv.database.SongEntity
import com.musicplayer.gdrivetv.playback.PlaybackService
import com.musicplayer.gdrivetv.sync.DriveSyncWorker
import com.musicplayer.gdrivetv.ui.screens.*
import com.musicplayer.gdrivetv.ui.theme.GDriveMusicTVTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private var mediaController: MediaController? = null

    // UI State Holders
    private var currentScreen by mutableStateOf<TvScreen>(TvScreen.Library)
    private var songs by mutableStateOf<List<SongEntity>>(emptyList())
    private var playlists by mutableStateOf<List<PlaylistEntity>>(emptyList())
    private var selectedPlaylist by mutableStateOf<PlaylistEntity?>(null)
    private var selectedPlaylistSongs by mutableStateOf<List<SongEntity>>(emptyList())
    private var currentSong by mutableStateOf<SongEntity?>(null)

    // Player Playback States
    private var isPlaying by mutableStateOf(false)
    private var progress by mutableStateOf(0f)
    private var playbackTimeStr by mutableStateOf("0:00")
    private var totalDurationStr by mutableStateOf("0:00")

    private val db by lazy { AppDatabase.getDatabase(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Initial configuration preferences
        val sharedPrefs = getSharedPreferences("gdrive_music_prefs", Context.MODE_PRIVATE)
        if (!sharedPrefs.contains("folder_id")) {
            sharedPrefs.edit().putString("folder_id", "1-mRwu6GyYVIX_AJYI2dP-HtY6Hoip3Zd").apply()
        }

        // 2. Setup periodic automated sync (every 6 hours)
        setupPeriodicSync()

        // 3. Bind Media Session Controller
        val sessionToken = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener({
            mediaController = controllerFuture.get()
            setupPlayerListener()
        }, MoreExecutors.directExecutor())

        // 4. Fetch initial database cache
        loadDbCache()

        // 5. Build Compose interface
        setContent {
            GDriveMusicTVTheme {
                val folderId = sharedPrefs.getString("folder_id", "1-mRwu6GyYVIX_AJYI2dP-HtY6Hoip3Zd") ?: "1-mRwu6GyYVIX_AJYI2dP-HtY6Hoip3Zd"
                val apiKey = sharedPrefs.getString("api_key", "AIzaSyDao07CoaqI09Mo9zsR2NzmGoH8XBaCqMY") ?: "AIzaSyDao07CoaqI09Mo9zsR2NzmGoH8XBaCqMY"

                MainScreen(
                    currentScreen = currentScreen,
                    onNavigate = { currentScreen = it }
                ) {
                    when (currentScreen) {
                        is TvScreen.Library -> {
                            LibraryScreen(
                                songs = songs,
                                onPlaySong = { playSong(it) },
                                onAddSongToPlaylist = { addSongToPlaylistDialog(it) }
                            )
                        }
                        is TvScreen.Playlists -> {
                            PlaylistScreen(
                                playlists = playlists,
                                selectedPlaylistSongs = selectedPlaylistSongs,
                                selectedPlaylist = selectedPlaylist,
                                onCreatePlaylist = { createPlaylist(it) },
                                onDeletePlaylist = { deletePlaylist(it) },
                                onSelectPlaylist = { selectPlaylist(it) },
                                onPlaySong = { playSong(it) },
                                onRemoveSong = { removeSongFromPlaylist(it) }
                            )
                        }
                        is TvScreen.NowPlaying -> {
                            NowPlayingScreen(
                                currentSong = currentSong,
                                isPlaying = isPlaying,
                                progress = progress,
                                playbackTimeStr = playbackTimeStr,
                                totalDurationStr = totalDurationStr,
                                onTogglePlay = { togglePlay() },
                                onSkipNext = { mediaController?.seekToNext() },
                                onSkipPrev = { mediaController?.seekToPrevious() },
                                isShuffle = mediaController?.shuffleModeEnabled ?: false,
                                onToggleShuffle = { mediaController?.shuffleModeEnabled = !(mediaController?.shuffleModeEnabled ?: false) },
                                isRepeat = (mediaController?.repeatMode ?: Player.REPEAT_MODE_OFF) != Player.REPEAT_MODE_OFF,
                                onToggleRepeat = {
                                    val currentMode = mediaController?.repeatMode ?: Player.REPEAT_MODE_OFF
                                    mediaController?.repeatMode = if (currentMode == Player.REPEAT_MODE_OFF) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_OFF
                                }
                            )
                        }
                        is TvScreen.Settings -> {
                            SettingsScreen(
                                onTriggerManualSync = { triggerManualSync() },
                                folderId = folderId,
                                onFolderIdChanged = { sharedPrefs.edit().putString("folder_id", it).apply() },
                                apiKey = apiKey,
                                onApiKeyChanged = { sharedPrefs.edit().putString("api_key", it).apply() }
                            )
                        }
                    }
                }
            }
        }

        // Start progress tracking routine
        startProgressTracker()
    }

    private fun setupPeriodicSync() {
        val syncRequest = PeriodicWorkRequestBuilder<DriveSyncWorker>(6, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "DriveSyncWork",
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )
    }

    private fun triggerManualSync() {
        val syncRequest = OneTimeWorkRequestBuilder<DriveSyncWorker>().build()
        WorkManager.getInstance(applicationContext).enqueue(syncRequest)
    }

    private fun loadDbCache() {
        lifecycleScope.launch {
            db.songDao().getAllSongs().collect { songsList ->
                songs = songsList
            }
        }
        lifecycleScope.launch {
            db.playlistDao().getAllPlaylists().collect { playlistsList ->
                playlists = playlistsList
            }
        }
    }

    private fun playSong(song: SongEntity) {
        val controller = mediaController ?: return
        currentSong = song

        // Media Item Configuration: Use local file if downloaded, else use streaming GDrive URL
        val mediaUri = if (song.isDownloaded && song.localFilePath != null) {
            song.localFilePath
        } else {
            song.gdriveUrl
        }

        val mediaItem = MediaItem.fromUri(mediaUri)
        controller.setMediaItem(mediaItem)
        controller.prepare()
        controller.play()
        isPlaying = true
        currentScreen = TvScreen.NowPlaying
    }

    private fun togglePlay() {
        val controller = mediaController ?: return
        if (controller.isPlaying) {
            controller.pause()
            isPlaying = false
        } else {
            controller.play()
            isPlaying = true
        }
    }

    private fun setupPlayerListener() {
        mediaController?.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlayingChanged: Boolean) {
                isPlaying = isPlayingChanged
            }
        })
    }

    private fun startProgressTracker() {
        lifecycleScope.launch {
            while (true) {
                withContext(Dispatchers.Main) {
                    val controller = mediaController
                    if (controller != null && controller.isPlaying) {
                        val pos = controller.currentPosition
                        val dur = controller.duration
                        if (dur > 0) {
                            progress = pos.toFloat() / dur.toFloat()
                            playbackTimeStr = formatTime(pos)
                            totalDurationStr = formatTime(dur)
                        }
                    }
                }
                kotlinx.coroutines.delay(1000)
            }
        }
    }

    private fun formatTime(ms: Long): String {
        val totalSecs = ms / 1000
        val mins = totalSecs / 60
        val secs = totalSecs % 60
        return String.format("%d:%02d", mins, secs)
    }

    // Playlist Database Interactions
    private fun createPlaylist(name: String) {
        lifecycleScope.launch {
            db.playlistDao().insertPlaylist(PlaylistEntity(name = name))
        }
    }

    private fun deletePlaylist(playlist: PlaylistEntity) {
        lifecycleScope.launch {
            db.playlistDao().deletePlaylist(playlist)
            if (selectedPlaylist?.id == playlist.id) {
                selectedPlaylist = null
                selectedPlaylistSongs = emptyList()
            }
        }
    }

    private fun selectPlaylist(playlist: PlaylistEntity) {
        selectedPlaylist = playlist
        lifecycleScope.launch {
            db.playlistDao().getSongsInPlaylist(playlist.id).collect { songList ->
                selectedPlaylistSongs = songList
            }
        }
    }

    private fun removeSongFromPlaylist(song: SongEntity) {
        val playlist = selectedPlaylist ?: return
        lifecycleScope.launch {
            db.playlistDao().removeSongFromPlaylist(playlist.id, song.id)
        }
    }

    private fun addSongToPlaylistDialog(song: SongEntity) {
        // If there's an active playlist selected, add it immediately for D-pad UX ease
        val activePlaylist = selectedPlaylist
        if (activePlaylist != null) {
            lifecycleScope.launch {
                db.playlistDao().addSongToPlaylist(
                    PlaylistSongCrossRef(playlistId = activePlaylist.id, songId = song.id)
                )
            }
        } else {
            // Otherwise add it to the first available playlist if any exists
            lifecycleScope.launch {
                val list = db.playlistDao().getAllPlaylists().first()
                if (list.isNotEmpty()) {
                    db.playlistDao().addSongToPlaylist(
                        PlaylistSongCrossRef(playlistId = list.first().id, songId = song.id)
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        if (::controllerFuture.isInitialized) {
            MediaController.releaseFuture(controllerFuture)
        }
        super.onDestroy()
    }
}
