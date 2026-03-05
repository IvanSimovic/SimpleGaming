package com.simovic.simplegaming.feature.album.domain.usecase

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.base.domain.result.mapSuccess
import com.simovic.simplegaming.feature.album.domain.model.Album
import com.simovic.simplegaming.feature.album.domain.repository.AlbumRepository

internal class GetAlbumListUseCase(
    private val albumRepository: AlbumRepository,
) {
    suspend operator fun invoke(query: String?): Result<List<Album>> {
        val result =
            albumRepository
                .searchAlbum(query)
                .mapSuccess {
                    val albumsWithImages = value.filter { it.getDefaultImageUrl() != null }

                    copy(value = albumsWithImages)
                }

        return result
    }
}
