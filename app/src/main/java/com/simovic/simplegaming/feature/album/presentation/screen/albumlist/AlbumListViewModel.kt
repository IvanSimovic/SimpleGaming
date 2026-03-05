package com.simovic.simplegaming.feature.album.presentation.screen.albumlist

import androidx.lifecycle.viewModelScope
import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.base.presentation.viewmodel.BaseViewModel
import com.simovic.simplegaming.feature.album.domain.usecase.GetAlbumListUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
internal class AlbumListViewModel(
    private val getAlbumListUseCase: GetAlbumListUseCase,
) : BaseViewModel<AlbumListUiState, AlbumListAction>(AlbumListUiState.Loading) {
    private val query = MutableStateFlow("")

    init {
        viewModelScope.launch {
            query
                .debounce(500)
                .distinctUntilChanged()
                .filter { it.isBlank() || it.length >= 2 }
                .collectLatest { currentQuery ->
                    sendAction(AlbumListAction.AlbumListLoadStart)
                    when (val result = getAlbumListUseCase(currentQuery)) {
                        is Result.Success -> {
                            val albums = result.value
                            sendAction(
                                if (albums.isEmpty()) {
                                    AlbumListAction.AlbumListLoadFailure
                                } else {
                                    AlbumListAction.AlbumListLoadSuccess(albums)
                                },
                            )
                        }
                        is Result.Failure -> sendAction(AlbumListAction.AlbumListLoadFailure)
                    }
                }
        }
    }

    fun onQueryChanged(newQuery: String) {
        query.value = newQuery.trim()
    }

    fun clearQuery() {
        query.value = ""
    }
}
