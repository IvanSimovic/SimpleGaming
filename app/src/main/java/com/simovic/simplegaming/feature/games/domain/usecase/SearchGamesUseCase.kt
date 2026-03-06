package com.simovic.simplegaming.feature.games.domain.usecase

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.games.domain.model.Game
import com.simovic.simplegaming.feature.games.domain.repository.GameSearchRepository

class SearchGamesUseCase(
    private val repo: GameSearchRepository,
) {
    suspend operator fun invoke(query: String): Result<List<Game>> = repo.searchGames(query)
}
