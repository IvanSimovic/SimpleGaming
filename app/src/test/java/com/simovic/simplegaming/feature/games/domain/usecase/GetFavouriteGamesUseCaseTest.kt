package com.simovic.simplegaming.feature.games.domain.usecase

import app.cash.turbine.test
import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.fake.FakeFavouriteGamesRepository
import com.simovic.simplegaming.feature.games.domain.model.FavouriteGame
import com.simovic.simplegaming.feature.games.domain.model.UserConfig
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeEqualTo
import com.simovic.simplegaming.fake.GAME_ID_1
import com.simovic.simplegaming.fake.GAME_NAME
import com.simovic.simplegaming.fake.VALID_USER_ID
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetFavouriteGamesUseCaseTest {
    private lateinit var repository: FakeFavouriteGamesRepository
    private lateinit var useCase: GetFavouriteGamesUseCase

    @BeforeEach
    fun setUp() {
        repository = FakeFavouriteGamesRepository()
    }

    @Test
    fun `blank userId returns an error immediately`() = runTest {
        useCase = GetFavouriteGamesUseCase(repository, UserConfig(userId = ""))

        useCase().test {
            awaitItem() shouldBeInstanceOf Result.Failure::class
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `whitespace userId returns an error immediately`() = runTest {
        useCase = GetFavouriteGamesUseCase(repository, UserConfig(userId = "   "))

        useCase().test {
            awaitItem() shouldBeInstanceOf Result.Failure::class
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `valid userId returns the user's favourite games`() = runTest {
        useCase = GetFavouriteGamesUseCase(repository, UserConfig(userId = VALID_USER_ID))
        val games = listOf(FavouriteGame(gameId = GAME_ID_1, name = GAME_NAME, imageUrl = "", addedAt = 0L))

        useCase().test {
            repository.emit(Result.Success(games))
            val result = awaitItem()
            result shouldBeInstanceOf Result.Success::class
            (result as Result.Success).value shouldBeEqualTo games
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `valid userId returns an error when the data source fails`() = runTest {
        useCase = GetFavouriteGamesUseCase(repository, UserConfig(userId = VALID_USER_ID))

        useCase().test {
            repository.emit(Result.Failure())
            awaitItem() shouldBeInstanceOf Result.Failure::class
            cancelAndIgnoreRemainingEvents()
        }
    }
}
