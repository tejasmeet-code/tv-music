package com.musicplayer.gdrivetv.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM playlists ORDER BY name ASC")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSongToPlaylist(crossRef: PlaylistSongCrossRef)

    @Query("DELETE FROM playlist_song_cross_ref WHERE playlistId = :playlistId AND songId = :songId")
    suspend fun removeSongFromPlaylist(playlistId: Long, songId: String)

    @Query("""
        SELECT songs.* FROM songs 
        INNER JOIN playlist_song_cross_ref ON songs.id = playlist_song_cross_ref.songId 
        WHERE playlist_song_cross_ref.playlistId = :playlistId 
        ORDER BY songs.title ASC
    """)
    fun getSongsInPlaylist(playlistId: Long): Flow<List<SongEntity>>

    @Query("""
        SELECT songs.* FROM songs 
        INNER JOIN playlist_song_cross_ref ON songs.id = playlist_song_cross_ref.songId 
        WHERE playlist_song_cross_ref.playlistId = :playlistId 
        ORDER BY songs.title ASC
    """)
    suspend fun getSongsInPlaylistList(playlistId: Long): List<SongEntity>
    @Query("SELECT * FROM playlists WHERE name = :name LIMIT 1")
    suspend fun getPlaylistByName(name: String): PlaylistEntity?

    @Query("""
        SELECT COUNT(*) FROM playlist_song_cross_ref 
        WHERE songId = :songId
    """)
    suspend fun getPlaylistCountForSong(songId: String): Int
}
