package com.simovic.simplegaming.feature.livefeed.domain.usecase

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.livefeed.domain.model.FeedItem
import com.simovic.simplegaming.feature.livefeed.domain.repository.LiveFeedRepo

internal class GetLiveFeedUseCase(
    private val repo: LiveFeedRepo,
) {
    suspend operator fun invoke(): Result<List<FeedItem>> =
        when (val result = repo.getFeed()) {
            is Result.Success -> result.value?.let { Result.Success(it) } ?: Result.Failure()
            is Result.Failure -> result
        }
}
