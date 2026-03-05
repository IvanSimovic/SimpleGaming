package com.simovic.simplegaming.feature.album.presentation.screen.albumdetail

import com.simovic.simplegaming.base.presentation.viewmodel.BaseAction
import com.simovic.simplegaming.feature.album.domain.model.Album

internal sealed interface AlbumDetailAction : BaseAction<AlbumDetailUiState> {
    object AlbumLoadStart : AlbumDetailAction {
        override fun reduce(state: AlbumDetailUiState) = AlbumDetailUiState.Loading
    }

    class AlbumLoadSuccess(
        private val album: Album,
        private val isFavorite: Boolean,
        private val mbIdOverride: String?,
    ) : AlbumDetailAction {
        override fun reduce(state: AlbumDetailUiState) =
            AlbumDetailUiState.Content(
                artistName = album.artist,
                albumName = album.name,
                coverImageUrl = album.getDefaultImageUrl() ?: "",
                tracks = album.tracks,
                tags = album.tags,
                mbId = mbIdOverride ?: album.mbId,
                isFavorite = isFavorite,
            )
    }

    object AlbumLoadFailure : AlbumDetailAction {
        override fun reduce(state: AlbumDetailUiState) = AlbumDetailUiState.Error
    }

    class ToggleFavoriteStatus(
        private val isFavorite: Boolean,
    ) : AlbumDetailAction {
        override fun reduce(state: AlbumDetailUiState): AlbumDetailUiState =
            (state as? AlbumDetailUiState.Content)?.copy(isFavorite = isFavorite) ?: state
    }
}
