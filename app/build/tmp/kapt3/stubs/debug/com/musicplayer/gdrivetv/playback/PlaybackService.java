package com.musicplayer.gdrivetv.playback;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0003J\b\u0010\u0013\u001a\u00020\u0012H\u0002J\b\u0010\u0014\u001a\u00020\u0012H\u0017J\b\u0010\u0015\u001a\u00020\u0012H\u0016J\u0012\u0010\u0016\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\b\u0010\u0019\u001a\u00020\u0012H\u0002J\b\u0010\u001a\u001a\u00020\u0012H\u0003R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0018\u00010\fR\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0018\u00010\u000fR\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcom/musicplayer/gdrivetv/playback/PlaybackService;", "Landroidx/media3/session/MediaSessionService;", "()V", "CHANNEL_ID", "", "NOTIFICATION_ID", "", "mediaSession", "Landroidx/media3/session/MediaSession;", "player", "Landroidx/media3/exoplayer/ExoPlayer;", "wakeLock", "Landroid/os/PowerManager$WakeLock;", "Landroid/os/PowerManager;", "wifiLock", "Landroid/net/wifi/WifiManager$WifiLock;", "Landroid/net/wifi/WifiManager;", "acquireLocks", "", "createNotificationChannel", "onCreate", "onDestroy", "onGetSession", "controllerInfo", "Landroidx/media3/session/MediaSession$ControllerInfo;", "releaseLocks", "startForegroundServiceWithNotification", "app_debug"})
public final class PlaybackService extends androidx.media3.session.MediaSessionService {
    @org.jetbrains.annotations.Nullable
    private androidx.media3.session.MediaSession mediaSession;
    @org.jetbrains.annotations.Nullable
    private androidx.media3.exoplayer.ExoPlayer player;
    @org.jetbrains.annotations.Nullable
    private android.os.PowerManager.WakeLock wakeLock;
    @org.jetbrains.annotations.Nullable
    private android.net.wifi.WifiManager.WifiLock wifiLock;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String CHANNEL_ID = "gdrive_music_channel";
    private final int NOTIFICATION_ID = 9001;
    
    public PlaybackService() {
        super();
    }
    
    @java.lang.Override
    @androidx.annotation.OptIn(markerClass = {androidx.media3.common.util.UnstableApi.class})
    public void onCreate() {
    }
    
    private final void createNotificationChannel() {
    }
    
    @android.annotation.SuppressLint(value = {"ForegroundServiceType"})
    private final void startForegroundServiceWithNotification() {
    }
    
    @android.annotation.SuppressLint(value = {"WakelockTimeout"})
    private final void acquireLocks() {
    }
    
    private final void releaseLocks() {
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public androidx.media3.session.MediaSession onGetSession(@org.jetbrains.annotations.NotNull
    androidx.media3.session.MediaSession.ControllerInfo controllerInfo) {
        return null;
    }
    
    @java.lang.Override
    public void onDestroy() {
    }
}