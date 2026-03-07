package com.simovic.simplegaming.feature.games.domain.usecase

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.games.domain.model.UserConfig
import com.simovic.simplegaming.feature.games.domain.repository.FavouriteGamesRepository

class GetFavouriteGameIdsUseCase(
    private val repository: FavouriteGamesRepository,
    private val userConfig: UserConfig,
) {
    suspend operator fun invoke(): Result<Set<String>> = repository.getFavouriteGameIds(userConfig.userId)
}
