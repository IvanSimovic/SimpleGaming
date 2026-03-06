package com.simovic.simplegaming.feature.games.data.repository

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.games.data.datasource.firestore.FavouriteGamesFirestore
import com.simovic.simplegaming.feature.games.data.mapper.FavouriteGameMapper
import com.simovic.simplegaming.feature.games.domain.model.FavouriteGame
import com.simovic.simplegaming.feature.games.domain.model.Game
import com.simovic.simplegaming.feature.games.domain.repository.FavouriteGamesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class FavouriteGamesRepositoryImpl(
    private val dataSource: FavouriteGamesFirestore,
    private val mapper: FavouriteGameMapper,
) : FavouriteGamesRepository {
    override fun getFavouriteGames(userId: String): Flow<Result<List<FavouriteGame>>> =
        dataSource.observeFavouriteGames(userId).map { result ->
            when (result) {
                is Result.Success -> Result.Success(result.value.map(mapper::toDomain))
                is Result.Failure -> result
            }
        }

    override suspend fun addFavouriteGame(
        userId: String,
        game: Game,
    ): Result<Unit> =
        try {
            dataSource.addFavouriteGame(userId, game)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }

    override suspend fun removeFavouriteGame(
        userId: String,
        gameId: String,
    ): Result<Unit> =
        try {
            dataSource.removeFavouriteGame(userId, gameId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }
}
