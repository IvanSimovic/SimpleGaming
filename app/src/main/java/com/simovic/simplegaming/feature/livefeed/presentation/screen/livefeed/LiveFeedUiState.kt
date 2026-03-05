package com.simovic.simplegaming.feature.livefeed.presentation.screen.livefeed

import androidx.compose.runtime.Immutable
import com.simovic.simplegaming.base.presentation.viewmodel.BaseState

@Immutable
internal sealed interface LiveFeedUiState : BaseState {
    @Immutable
    data class Content(
        val feed: List<FeedListItemUiModel>,
    ) : LiveFeedUiState

    @Immutable
    data object Loading : LiveFeedUiState

    @Immutable
    data object Error : LiveFeedUiState
}
