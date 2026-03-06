package com.simovic.simplegaming.feature.games.domain.usecase

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.games.domain.model.Game
import com.simovic.simplegaming.feature.games.domain.model.UserConfig
import com.simovic.simplegaming.feature.games.domain.repository.FavouriteGamesRepository

class AddFavouriteGameUseCase(
    private val repo: FavouriteGamesRepository,
    private val userConfig: UserConfig,
) {
    suspend operator fun invoke(game: Game): Result<Unit> =
        if (userConfig.userId.isBlank()) {
            Result.Failure()
        } else {
            repo.addFavouriteGame(userConfig.userId, game)
        }
}
