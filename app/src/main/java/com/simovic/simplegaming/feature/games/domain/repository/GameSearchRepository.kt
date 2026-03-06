package com.simovic.simplegaming.feature.games.domain.repository

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.games.domain.model.Game

interface GameSearchRepository {
    suspend fun searchGames(query: String): Result<List<Game>>
}
