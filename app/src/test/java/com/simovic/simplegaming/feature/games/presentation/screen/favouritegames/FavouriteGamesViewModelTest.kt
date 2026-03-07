package com.simovic.simplegaming.feature.games.presentation.screen.favouritegames

import app.cash.turbine.test
import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.fake.FakeFavouriteGamesRepository
import com.simovic.simplegaming.fake.GAME_ID_1
import com.simovic.simplegaming.fake.GAME_ID_2
import com.simovic.simplegaming.fake.GAME_NAME
import com.simovic.simplegaming.fake.VALID_USER_ID
import com.simovic.simplegaming.feature.games.domain.model.FavouriteGame
import com.simovic.simplegaming.feature.games.domain.model.UserConfig
import com.simovic.simplegaming.feature.games.domain.usecase.GetFavouriteGamesUseCase
import com.simovic.simplegaming.feature.games.domain.usecase.RemoveFavouriteGameUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavouriteGamesViewModelTest {
    private lateinit var repository: FakeFavouriteGamesRepository
    private lateinit var viewModel: FavouriteGamesViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repository = FakeFavouriteGamesRepository()
        val userConfig = UserConfig(VALID_USER_ID)
        viewModel = FavouriteGamesViewModel(
            getFavouriteGames = GetFavouriteGamesUseCase(repository, userConfig),
            removeFavouriteGame = RemoveFavouriteGameUseCase(repository, userConfig),
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() {
        viewModel.uiStateFlow.value shouldBeInstanceOf FavouriteGamesUiState.Loading::class
    }

    @Test
    fun `games load successfully - screen shows the list`() = runTest {
        val game = FavouriteGame(gameId = GAME_ID_1, name = GAME_NAME, imageUrl = "", addedAt = 0L)
        val expectedUiModel = FavouriteGameUiModel(gameId = GAME_ID_1, name = GAME_NAME, imageUrl = "")

        viewModel.uiStateFlow.test {
            awaitItem() // Loading
            repository.emit(Result.Success(listOf(game)))
            awaitItem() shouldBeEqualTo FavouriteGamesUiState.Content(listOf(expectedUiModel))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `user has no favourite games - screen shows empty state`() = runTest {
        viewModel.uiStateFlow.test {
            awaitItem() // Loading
            repository.emit(Result.Success(emptyList()))
            awaitItem() shouldBeInstanceOf FavouriteGamesUiState.Empty::class
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `games fail to load - screen shows an error`() = runTest {
        viewModel.uiStateFlow.test {
            awaitItem() // Loading
            repository.emit(Result.Failure())
            awaitItem() shouldBeInstanceOf FavouriteGamesUiState.Error::class
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `updating the game list refreshes the screen`() = runTest {
        val game1 = FavouriteGame(gameId = GAME_ID_1, name = GAME_NAME, imageUrl = "", addedAt = 0L)
        val game2 = FavouriteGame(gameId = GAME_ID_2, name = GAME_NAME, imageUrl = "", addedAt = 0L)

        viewModel.uiStateFlow.test {
            awaitItem() // Loading
            repository.emit(Result.Success(listOf(game1)))
            awaitItem() shouldBeEqualTo FavouriteGamesUiState.Content(
                listOf(FavouriteGameUiModel(gameId = GAME_ID_1, name = GAME_NAME, imageUrl = "")),
            )
            repository.emit(Result.Success(listOf(game1, game2)))
            awaitItem() shouldBeEqualTo FavouriteGamesUiState.Content(
                listOf(
                    FavouriteGameUiModel(gameId = GAME_ID_1, name = GAME_NAME, imageUrl = ""),
                    FavouriteGameUiModel(gameId = GAME_ID_2, name = GAME_NAME, imageUrl = ""),
                ),
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `removing a game removes the correct one`() = runTest {
        viewModel.removeGame(GAME_ID_1)

        repository.removedGameIds shouldBeEqualTo listOf(GAME_ID_1)
    }
}
