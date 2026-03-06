package com.simovic.simplegaming.feature.games.data.repository

import com.simovic.simplegaming.base.data.retrofit.apiresult.ApiResult
import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.games.data.datasource.api.service.GamesApiService
import com.simovic.simplegaming.feature.games.data.mapper.GameMapper
import com.simovic.simplegaming.feature.games.domain.model.Game
import com.simovic.simplegaming.feature.games.domain.repository.GameSearchRepository

internal class GameSearchRepositoryImpl(
    private val apiService: GamesApiService,
    private val mapper: GameMapper,
) : GameSearchRepository {
    override suspend fun searchGames(query: String): Result<List<Game>> =
        when (val result = apiService.searchGames(query)) {
            is ApiResult.Success -> Result.Success(result.data.results.map(mapper::toDomain))
            is ApiResult.Error -> Result.Failure()
            is ApiResult.Exception -> Result.Failure(result.throwable)
        }
}
