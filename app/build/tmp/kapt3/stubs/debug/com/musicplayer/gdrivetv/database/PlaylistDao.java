package com.musicplayer.gdrivetv.database;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\u0019\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u0019\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ\u0014\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\r0\fH\'J\u001c\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\r0\f2\u0006\u0010\u0010\u001a\u00020\u0011H\'J\u001f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u000f0\r2\u0006\u0010\u0010\u001a\u00020\u0011H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013J\u0019\u0010\u0014\u001a\u00020\u00112\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ!\u0010\u0015\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u0017H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0018\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0019"}, d2 = {"Lcom/musicplayer/gdrivetv/database/PlaylistDao;", "", "addSongToPlaylist", "", "crossRef", "Lcom/musicplayer/gdrivetv/database/PlaylistSongCrossRef;", "(Lcom/musicplayer/gdrivetv/database/PlaylistSongCrossRef;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deletePlaylist", "playlist", "Lcom/musicplayer/gdrivetv/database/PlaylistEntity;", "(Lcom/musicplayer/gdrivetv/database/PlaylistEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllPlaylists", "Lkotlinx/coroutines/flow/Flow;", "", "getSongsInPlaylist", "Lcom/musicplayer/gdrivetv/database/SongEntity;", "playlistId", "", "getSongsInPlaylistList", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertPlaylist", "removeSongFromPlaylist", "songId", "", "(JLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao
public abstract interface PlaylistDao {
    
    @androidx.room.Query(value = "SELECT * FROM playlists ORDER BY name ASC")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.musicplayer.gdrivetv.database.PlaylistEntity>> getAllPlaylists();
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insertPlaylist(@org.jetbrains.annotations.NotNull
    com.musicplayer.gdrivetv.database.PlaylistEntity playlist, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Delete
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object deletePlaylist(@org.jetbrains.annotations.NotNull
    com.musicplayer.gdrivetv.database.PlaylistEntity playlist, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object addSongToPlaylist(@org.jetbrains.annotations.NotNull
    com.musicplayer.gdrivetv.database.PlaylistSongCrossRef crossRef, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM playlist_song_cross_ref WHERE playlistId = :playlistId AND songId = :songId")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object removeSongFromPlaylist(long playlistId, @org.jetbrains.annotations.NotNull
    java.lang.String songId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "\n        SELECT songs.* FROM songs \n        INNER JOIN playlist_song_cross_ref ON songs.id = playlist_song_cross_ref.songId \n        WHERE playlist_song_cross_ref.playlistId = :playlistId \n        ORDER BY songs.title ASC\n    ")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.musicplayer.gdrivetv.database.SongEntity>> getSongsInPlaylist(long playlistId);
    
    @androidx.room.Query(value = "\n        SELECT songs.* FROM songs \n        INNER JOIN playlist_song_cross_ref ON songs.id = playlist_song_cross_ref.songId \n        WHERE playlist_song_cross_ref.playlistId = :playlistId \n        ORDER BY songs.title ASC\n    ")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getSongsInPlaylistList(long playlistId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.musicplayer.gdrivetv.database.SongEntity>> $completion);
}