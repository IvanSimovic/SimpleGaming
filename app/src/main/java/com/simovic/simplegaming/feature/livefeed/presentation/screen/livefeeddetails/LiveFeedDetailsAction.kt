package com.simovic.simplegaming.feature.livefeed.presentation.screen.livefeeddetails

import com.simovic.simplegaming.base.presentation.viewmodel.BaseAction
import com.simovic.simplegaming.feature.livefeed.domain.model.FeedItem

internal sealed interface LiveFeedDetailsAction : BaseAction<LiveFeedDetailsUiState> {
    class Content(
        private val feed: FeedItem,
    ) : LiveFeedDetailsAction {
        override fun reduce(state: LiveFeedDetailsUiState) = LiveFeedDetailsUiState.Content(feed)
    }
}
