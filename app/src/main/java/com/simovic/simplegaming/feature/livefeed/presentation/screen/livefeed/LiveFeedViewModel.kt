package com.simovic.simplegaming.feature.livefeed.presentation.screen.livefeed

import androidx.lifecycle.viewModelScope
import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.base.presentation.viewmodel.BaseViewModel
import com.simovic.simplegaming.feature.livefeed.domain.usecase.GetLiveFeedUseCase
import kotlinx.coroutines.launch

internal class LiveFeedViewModel(
    private val getLiveFeedUseCase: GetLiveFeedUseCase,
) : BaseViewModel<LiveFeedUiState, LiveFeedAction>(LiveFeedUiState.Loading) {
    init {
        viewModelScope.launch {
            when (val result = getLiveFeedUseCase()) {
                is Result.Success -> sendAction(LiveFeedAction.Success(result.value.toFeedListModel()))
                is Result.Failure -> sendAction(LiveFeedAction.AlbumListLoadFailure)
            }
        }
    }
}
