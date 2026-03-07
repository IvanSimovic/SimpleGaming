package com.simovic.simplegaming.fake

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.games.domain.model.FavouriteGame
import com.simovic.simplegaming.feature.games.domain.model.Game
import com.simovic.simplegaming.feature.games.domain.repository.FavouriteGamesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeFavouriteGamesRepository : FavouriteGamesRepository {
    private val flow = MutableSharedFlow<Result<List<FavouriteGame>>>()
    var favouriteGameIdsResult: Result<Set<String>> = Result.Success(emptySet())
    val addedGames = mutableListOf<Game>()
    val removedGameIds = mutableListOf<String>()

    suspend fun emit(result: Result<List<FavouriteGame>>) = flow.emit(result)

    override fun getFavouriteGames(userId: String): Flow<Result<List<FavouriteGame>>> = flow

    override suspend fun getFavouriteGameIds(userId: String): Result<Set<String>> = favouriteGameIdsResult

    override suspend fun addFavouriteGame(userId: String, game: Game): Result<Unit> {
        addedGames.add(game)
        return Result.Success(Unit)
    }

    override suspend fun removeFavouriteGame(userId: String, gameId: String): Result<Unit> {
        removedGameIds.add(gameId)
        return Result.Success(Unit)
    }
}
