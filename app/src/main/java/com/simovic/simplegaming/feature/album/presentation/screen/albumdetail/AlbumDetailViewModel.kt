package com.simovic.simplegaming.feature.album.presentation.screen.albumdetail

import androidx.lifecycle.viewModelScope
import com.simovic.simplegaming.base.domain.result.Result.Failure
import com.simovic.simplegaming.base.domain.result.Result.Success
import com.simovic.simplegaming.base.presentation.viewmodel.BaseViewModel
import com.simovic.simplegaming.feature.album.domain.usecase.GetAlbumUseCase
import com.simovic.simplegaming.feature.album.domain.usecase.IsAlbumFavoriteUseCase
import com.simovic.simplegaming.feature.album.domain.usecase.ToggleAlbumFavoriteStatusUseCase
import kotlinx.coroutines.launch

internal class AlbumDetailViewModel(
    private val getAlbumUseCase: GetAlbumUseCase,
    private val toggleAlbumFavoriteStatusUseCase: ToggleAlbumFavoriteStatusUseCase,
    private val isAlbumFavoriteUseCase: IsAlbumFavoriteUseCase,
) : BaseViewModel<AlbumDetailUiState, AlbumDetailAction>(AlbumDetailUiState.Loading) {
    fun onInit(
        albumName: String,
        artistName: String,
        albumMbId: String?,
    ) {
        getAlbum(albumName, artistName, albumMbId)
    }

    private fun getAlbum(
        albumName: String,
        artistName: String,
        albumMbId: String?,
    ) {
        sendAction(AlbumDetailAction.AlbumLoadStart)

        viewModelScope.launch {
            getAlbumUseCase(artistName, albumName, albumMbId).also {
                when (it) {
                    is Success -> {
                        val isFavorite = albumMbId?.let { mbId -> isAlbumFavoriteUseCase(mbId) } ?: false
                        sendAction(AlbumDetailAction.AlbumLoadSuccess(it.value, isFavorite, albumMbId))
                    }

                    is Failure -> {
                        sendAction(AlbumDetailAction.AlbumLoadFailure)
                    }
                }
            }
        }
    }

    fun addToFavorites() {
        viewModelScope.launch {
            val content = (uiStateFlow.value as? AlbumDetailUiState.Content)
            content?.let {
                it.mbId?.let { mbId ->
                    when (toggleAlbumFavoriteStatusUseCase(mbId, it.isFavorite)) {
                        is Success -> {
                            val newFavoriteStatus = isAlbumFavoriteUseCase(mbId)
                            sendAction(AlbumDetailAction.ToggleFavoriteStatus(newFavoriteStatus))
                        }

                        is Failure -> {
                            sendAction(AlbumDetailAction.AlbumLoadFailure)
                        }
                    }
                }
            }
        }
    }
}
