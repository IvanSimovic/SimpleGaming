package com.simovic.simplegaming.feature.games.domain.repository

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.games.domain.model.FavouriteGame
import com.simovic.simplegaming.feature.games.domain.model.Game
import kotlinx.coroutines.flow.Flow

interface FavouriteGamesRepository {
    fun getFavouriteGames(userId: String): Flow<Result<List<FavouriteGame>>>

    suspend fun getFavouriteGameIds(userId: String): Result<Set<String>>

    suspend fun addFavouriteGame(
        userId: String,
        game: Game,
    ): Result<Unit>

    suspend fun removeFavouriteGame(
        userId: String,
        gameId: String,
    ): Result<Unit>
}
