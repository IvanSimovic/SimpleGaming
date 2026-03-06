package com.simovic.simplegaming.feature.reels.data.repository

import com.simovic.simplegaming.base.data.retrofit.apiresult.ApiResult
import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.reels.data.datasource.api.service.GameDetailApiService
import com.simovic.simplegaming.feature.reels.data.mapper.ReelGameMapper
import com.simovic.simplegaming.feature.reels.domain.model.ReelGame
import com.simovic.simplegaming.feature.reels.domain.repository.GameDetailRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

internal class GameDetailRepositoryImpl(
    private val apiService: GameDetailApiService,
    private val mapper: ReelGameMapper,
) : GameDetailRepository {
    override suspend fun getReelGame(id: String): Result<ReelGame> =
        coroutineScope {
            val detailDeferred = async { apiService.getGameDetail(id) }
            val screenshotsDeferred = async { apiService.getGameScreenshots(id) }

            val detailResult = detailDeferred.await()
            val screenshotsResult = screenshotsDeferred.await()

            if (detailResult is ApiResult.Success) {
                val screenshots =
                    if (screenshotsResult is ApiResult.Success) {
                        screenshotsResult.data.results.map { it.image }
                    } else {
                        emptyList()
                    }
                Result.Success(mapper.toDomain(detailResult.data, screenshots))
            } else if (detailResult is ApiResult.Exception) {
                Result.Failure(detailResult.throwable)
            } else {
                Result.Failure()
            }
        }
}
