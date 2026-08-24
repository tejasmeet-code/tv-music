package com.musicplayer.gdrivetv.ui.screens;

@kotlin.Metadata(mv = {1, 8, 0}, k = 2, xi = 48, d1 = {"\u0000*\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u001aH\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\u00062\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\u00062\b\b\u0002\u0010\b\u001a\u00020\tH\u0007\u001a,\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u00042\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\rH\u0007\u00a8\u0006\u000f"}, d2 = {"LibraryScreen", "", "songs", "", "Lcom/musicplayer/gdrivetv/database/SongEntity;", "onPlaySong", "Lkotlin/Function1;", "onAddSongToPlaylist", "modifier", "Landroidx/compose/ui/Modifier;", "SongGridItem", "song", "onPlay", "Lkotlin/Function0;", "onAddToPlaylist", "app_debug"})
public final class LibraryScreenKt {
    
    @androidx.compose.runtime.Composable
    public static final void LibraryScreen(@org.jetbrains.annotations.NotNull
    java.util.List<com.musicplayer.gdrivetv.database.SongEntity> songs, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.musicplayer.gdrivetv.database.SongEntity, kotlin.Unit> onPlaySong, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.musicplayer.gdrivetv.database.SongEntity, kotlin.Unit> onAddSongToPlaylist, @org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void SongGridItem(@org.jetbrains.annotations.NotNull
    com.musicplayer.gdrivetv.database.SongEntity song, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onPlay, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onAddToPlaylist) {
    }
}