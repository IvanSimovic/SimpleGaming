package com.simovic.simplegaming.feature.album.data.datasource.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_albums")
internal data class FavoriteAlbumRoomModel(
    @PrimaryKey val mbId: String,
)
