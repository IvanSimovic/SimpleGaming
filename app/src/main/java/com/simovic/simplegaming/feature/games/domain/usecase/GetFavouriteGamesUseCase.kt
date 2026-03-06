package com.simovic.simplegaming.feature.games.domain.usecase

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.games.domain.model.FavouriteGame
import com.simovic.simplegaming.feature.games.domain.model.UserConfig
import com.simovic.simplegaming.feature.games.domain.repository.FavouriteGamesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetFavouriteGamesUseCase(
    private val repo: FavouriteGamesRepository,
    private val userConfig: UserConfig,
) {
    operator fun invoke(): Flow<Result<List<FavouriteGame>>> =
        if (userConfig.userId.isBlank()) {
            flowOf(Result.Failure())
        } else {
            repo.getFavouriteGames(userConfig.userId)
        }
}
