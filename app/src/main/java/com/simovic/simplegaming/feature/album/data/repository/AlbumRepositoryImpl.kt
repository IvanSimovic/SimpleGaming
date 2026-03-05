package com.simovic.simplegaming.feature.album.data.repository

import com.simovic.simplegaming.base.data.retrofit.apiresult.ApiResult
import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.album.data.datasource.api.service.AlbumRetrofitService
import com.simovic.simplegaming.feature.album.data.datasource.database.AlbumDao
import com.simovic.simplegaming.feature.album.data.datasource.database.model.FavoriteAlbumRoomModel
import com.simovic.simplegaming.feature.album.data.mapper.AlbumMapper
import com.simovic.simplegaming.feature.album.domain.model.Album
import com.simovic.simplegaming.feature.album.domain.repository.AlbumRepository
import timber.log.Timber

internal class AlbumRepositoryImpl(
    private val albumRetrofitService: AlbumRetrofitService,
    private val albumDao: AlbumDao,
    private val albumMapper: AlbumMapper,
) : AlbumRepository {
    override suspend fun searchAlbum(phrase: String?): Result<List<Album>> =
        when (val apiResult = albumRetrofitService.searchAlbumAsync(phrase)) {
            is ApiResult.Success -> {
                val albums =
                    apiResult
                        .data
                        .results
                        .albumMatches
                        .album
                        .also { albumsApiModels ->
                            val albumsRoomModels = albumsApiModels.map { albumMapper.apiToRoom(it) }
                            albumDao.insertAlbums(albumsRoomModels)
                        }.map { albumMapper.apiToDomain(it) }

                Result.Success(albums)
            }

            is ApiResult.Error -> {
                Result.Failure()
            }

            is ApiResult.Exception -> {
                Timber.e(apiResult.throwable)

                val albums =
                    albumDao
                        .getAll()
                        .map { albumMapper.roomToDomain(it) }

                Result.Success(albums)
            }
        }

    override suspend fun getAlbumInfo(
        artistName: String,
        albumName: String,
        mbId: String?,
    ): Result<Album> =
        when (val apiResult = albumRetrofitService.getAlbumInfoAsync(artistName, albumName, mbId)) {
            is ApiResult.Success -> {
                val album =
                    apiResult
                        .data
                        .album
                        .let { albumMapper.apiToDomain(it) }

                Result.Success(album)
            }

            is ApiResult.Error -> {
                Result.Failure()
            }

            is ApiResult.Exception -> {
                Timber.e(apiResult.throwable)

                val album =
                    albumDao
                        .getAlbum(artistName, albumName, mbId)
                        .let { albumMapper.roomToDomain(it) }

                Result.Success(album)
            }
        }

    override suspend fun addAlbumToFavorites(albumMbId: String): Result<Unit> =
        try {
            albumDao.insertFavoriteAlbum(FavoriteAlbumRoomModel(albumMbId))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }

    override suspend fun removeAlbumFromFavorites(albumMbId: String): Result<Unit> =
        try {
            albumDao.deleteFavoriteAlbum(FavoriteAlbumRoomModel(albumMbId))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }

    override suspend fun isAlbumFavorite(albumMbId: String): Boolean = albumDao.isAlbumFavorite(albumMbId)
}
