package com.simovic.simplegaming.feature.reels.domain.usecase

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.reels.domain.repository.ReelGamesRepository

class GetReelGameIdsUseCase(
    private val repository: ReelGamesRepository,
) {
    fun reset() = repository.resetPaging()

    suspend operator fun invoke(
        pageSize: Int,
        savedIds: Set<String>,
    ): Result<List<String>> =
        when (val result = repository.getNextReelGameIds(pageSize)) {
            is Result.Success -> Result.Success(result.value.filter { it !in savedIds })
            is Result.Failure -> result
        }
}
