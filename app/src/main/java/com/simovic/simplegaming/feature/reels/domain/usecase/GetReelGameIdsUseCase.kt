package com.simovic.simplegaming.feature.reels.domain.usecase

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.games.domain.model.UserConfig
import com.simovic.simplegaming.feature.games.domain.repository.FavouriteGamesRepository
import com.simovic.simplegaming.feature.reels.domain.repository.ReelGamesRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GetReelGameIdsUseCase(
    private val reelGamesRepository: ReelGamesRepository,
    private val favouriteGamesRepository: FavouriteGamesRepository,
    private val userConfig: UserConfig,
) {
    suspend operator fun invoke(): Result<List<String>> =
        coroutineScope {
            val allIdsDeferred = async { reelGamesRepository.getReelGameIds() }
            val savedIdsDeferred = async { favouriteGamesRepository.getFavouriteGameIds(userConfig.userId) }
            buildResult(
                allIdsResult = allIdsDeferred.await(),
                savedIdsResult = savedIdsDeferred.await(),
            )
        }

    private fun buildResult(
        allIdsResult: Result<List<String>>,
        savedIdsResult: Result<Set<String>>,
    ): Result<List<String>> {
        if (allIdsResult is Result.Failure) return allIdsResult
        val allIds = (allIdsResult as Result.Success).value
        val savedIds = if (savedIdsResult is Result.Success) savedIdsResult.value else emptySet()
        return Result.Success(allIds.filter { it !in savedIds })
    }
}
