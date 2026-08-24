package com.musicplayer.gdrivetv.ui.screens;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b7\u0018\u00002\u00020\u0001:\u0004\u0007\b\t\nB\u000f\b\u0004\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u0082\u0001\u0004\u000b\f\r\u000e\u00a8\u0006\u000f"}, d2 = {"Lcom/musicplayer/gdrivetv/ui/screens/TvScreen;", "", "name", "", "(Ljava/lang/String;)V", "getName", "()Ljava/lang/String;", "Library", "NowPlaying", "Playlists", "Settings", "Lcom/musicplayer/gdrivetv/ui/screens/TvScreen$Library;", "Lcom/musicplayer/gdrivetv/ui/screens/TvScreen$NowPlaying;", "Lcom/musicplayer/gdrivetv/ui/screens/TvScreen$Playlists;", "Lcom/musicplayer/gdrivetv/ui/screens/TvScreen$Settings;", "app_debug"})
public abstract class TvScreen {
    @org.jetbrains.annotations.NotNull
    private final java.lang.String name = null;
    
    private TvScreen(java.lang.String name) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getName() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/musicplayer/gdrivetv/ui/screens/TvScreen$Library;", "Lcom/musicplayer/gdrivetv/ui/screens/TvScreen;", "()V", "app_debug"})
    public static final class Library extends com.musicplayer.gdrivetv.ui.screens.TvScreen {
        @org.jetbrains.annotations.NotNull
        public static final com.musicplayer.gdrivetv.ui.screens.TvScreen.Library INSTANCE = null;
        
        private Library() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/musicplayer/gdrivetv/ui/screens/TvScreen$NowPlaying;", "Lcom/musicplayer/gdrivetv/ui/screens/TvScreen;", "()V", "app_debug"})
    public static final class NowPlaying extends com.musicplayer.gdrivetv.ui.screens.TvScreen {
        @org.jetbrains.annotations.NotNull
        public static final com.musicplayer.gdrivetv.ui.screens.TvScreen.NowPlaying INSTANCE = null;
        
        private NowPlaying() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/musicplayer/gdrivetv/ui/screens/TvScreen$Playlists;", "Lcom/musicplayer/gdrivetv/ui/screens/TvScreen;", "()V", "app_debug"})
    public static final class Playlists extends com.musicplayer.gdrivetv.ui.screens.TvScreen {
        @org.jetbrains.annotations.NotNull
        public static final com.musicplayer.gdrivetv.ui.screens.TvScreen.Playlists INSTANCE = null;
        
        private Playlists() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/musicplayer/gdrivetv/ui/screens/TvScreen$Settings;", "Lcom/musicplayer/gdrivetv/ui/screens/TvScreen;", "()V", "app_debug"})
    public static final class Settings extends com.musicplayer.gdrivetv.ui.screens.TvScreen {
        @org.jetbrains.annotations.NotNull
        public static final com.musicplayer.gdrivetv.ui.screens.TvScreen.Settings INSTANCE = null;
        
        private Settings() {
        }
    }
}