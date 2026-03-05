package com.simovic.simplegaming.feature.livefeed.domain.repository

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.livefeed.domain.model.FeedItem

internal interface LiveFeedRepo {
    suspend fun getFeed(): Result<List<FeedItem>?>
}
