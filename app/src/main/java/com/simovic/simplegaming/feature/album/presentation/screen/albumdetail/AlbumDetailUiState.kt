package com.simovic.simplegaming.feature.album.presentation.screen.albumdetail

import androidx.compose.runtime.Immutable
import com.simovic.simplegaming.base.presentation.viewmodel.BaseState
import com.simovic.simplegaming.feature.album.domain.model.Tag
import com.simovic.simplegaming.feature.album.domain.model.Track

@Immutable
internal sealed interface AlbumDetailUiState : BaseState {
    @Immutable
    data class Content(
        val albumName: String = "",
        val artistName: String = "",
        val coverImageUrl: String = "",
        val tracks: List<Track>? = emptyList(),
        val tags: List<Tag>? = emptyList(),
        val mbId: String? = null,
        val isFavorite: Boolean = false,
    ) : AlbumDetailUiState

    @Immutable
    data object Loading : AlbumDetailUiState

    @Immutable
    data object Error : AlbumDetailUiState
}
