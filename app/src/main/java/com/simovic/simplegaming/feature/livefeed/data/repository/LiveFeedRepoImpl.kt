package com.simovic.simplegaming.feature.livefeed.data.repository

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.livefeed.data.datasource.api.service.LiveFeedService
import com.simovic.simplegaming.feature.livefeed.data.mapper.LiveFeedMapper
import com.simovic.simplegaming.feature.livefeed.domain.model.FeedItem
import com.simovic.simplegaming.feature.livefeed.domain.repository.LiveFeedRepo
import timber.log.Timber

internal class LiveFeedRepoImpl(
    private val service: LiveFeedService,
    private val mapper: LiveFeedMapper,
) : LiveFeedRepo {
    override suspend fun getFeed(): Result<List<FeedItem>?> =
        try {
            Result.Success(mapper.rssToDomain(service.getFeed()))
        } catch (e: Exception) {
            Timber.d("::DEVELOP:: Exception is ${e.message}")
            Result.Failure(e)
        }
}
