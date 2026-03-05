package com.simovic.simplegaming.feature.livefeed.presentation.screen.livefeed

import com.simovic.simplegaming.feature.livefeed.domain.model.FeedItem

private const val PREVIEW_LENGTH = 20

data class FeedListItemUiModel(
    val feedItem: FeedItem,
    val preview: String,
)

fun List<FeedItem>.toFeedListModel() = this.map { it.toFeedListModel() }

fun FeedItem.toFeedListModel() =
    FeedListItemUiModel(
        feedItem = this,
        preview = description.take(PREVIEW_LENGTH),
    )
