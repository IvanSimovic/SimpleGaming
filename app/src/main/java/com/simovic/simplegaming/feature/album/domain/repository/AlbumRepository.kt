package com.simovic.simplegaming.feature.album.domain.repository

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.album.domain.model.Album

internal interface AlbumRepository {
    suspend fun getAlbumInfo(
        artistName: String,
        albumName: String,
        mbId: String?,
    ): Result<Album>

    suspend fun searchAlbum(phrase: String?): Result<List<Album>>

    suspend fun addAlbumToFavorites(albumMbId: String): Result<Unit>

    suspend fun removeAlbumFromFavorites(albumMbId: String): Result<Unit>

    suspend fun isAlbumFavorite(albumMbId: String): Boolean
}
