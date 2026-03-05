package com.simovic.simplegaming.feature.album.domain.usecase

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.album.domain.repository.AlbumRepository

internal class ToggleAlbumFavoriteStatusUseCase(
    private val albumRepository: AlbumRepository,
) {
    suspend operator fun invoke(
        albumMbId: String,
        isFavorite: Boolean,
    ): Result<Unit> =
        if (isFavorite) {
            albumRepository.removeAlbumFromFavorites(albumMbId)
        } else {
            albumRepository.addAlbumToFavorites(albumMbId)
        }
}
