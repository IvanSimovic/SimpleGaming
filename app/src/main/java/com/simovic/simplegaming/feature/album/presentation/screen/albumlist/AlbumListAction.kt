package com.simovic.simplegaming.feature.album.presentation.screen.albumlist

import com.simovic.simplegaming.base.presentation.viewmodel.BaseAction
import com.simovic.simplegaming.feature.album.domain.model.Album

internal sealed interface AlbumListAction : BaseAction<AlbumListUiState> {
    object AlbumListLoadStart : AlbumListAction {
        override fun reduce(state: AlbumListUiState) = AlbumListUiState.Loading
    }

    class AlbumListLoadSuccess(
        private val albums: List<Album>,
    ) : AlbumListAction {
        override fun reduce(state: AlbumListUiState) = AlbumListUiState.Content(albums)
    }

    object AlbumListLoadFailure : AlbumListAction {
        override fun reduce(state: AlbumListUiState) = AlbumListUiState.Error
    }
}
