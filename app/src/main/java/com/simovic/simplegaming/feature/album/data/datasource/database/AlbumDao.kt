package com.simovic.simplegaming.feature.album.data.datasource.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.simovic.simplegaming.feature.album.data.datasource.database.model.AlbumRoomModel
import com.simovic.simplegaming.feature.album.data.datasource.database.model.FavoriteAlbumRoomModel

@Dao
internal interface AlbumDao {
    @Query("SELECT * FROM albums")
    suspend fun getAll(): List<AlbumRoomModel>

    @Query("SELECT * FROM albums where artist = :artistName and name = :albumName and mbId = :mbId")
    suspend fun getAlbum(
        artistName: String,
        albumName: String,
        mbId: String?,
    ): AlbumRoomModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbums(albums: List<AlbumRoomModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteAlbum(favoriteAlbum: FavoriteAlbumRoomModel)

    @Delete
    suspend fun deleteFavoriteAlbum(favoriteAlbum: FavoriteAlbumRoomModel)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_albums WHERE mbId = :mbId LIMIT 1)")
    suspend fun isAlbumFavorite(mbId: String): Boolean
}
