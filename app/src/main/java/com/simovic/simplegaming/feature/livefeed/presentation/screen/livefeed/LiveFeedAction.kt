package com.simovic.simplegaming.feature.livefeed.presentation.screen.livefeed

import com.simovic.simplegaming.base.presentation.viewmodel.BaseAction

internal sealed interface LiveFeedAction : BaseAction<LiveFeedUiState> {
    object Start : LiveFeedAction {
        override fun reduce(state: LiveFeedUiState) = LiveFeedUiState.Loading
    }

    class Success(
        private val feed: List<FeedListItemUiModel>,
    ) : LiveFeedAction {
        override fun reduce(state: LiveFeedUiState) = LiveFeedUiState.Content(feed)
    }

    object AlbumListLoadFailure : LiveFeedAction {
        override fun reduce(state: LiveFeedUiState) = LiveFeedUiState.Error
    }
}
