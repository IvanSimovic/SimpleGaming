package com.simovic.simplegaming.feature.reels.presentation.screen.reels

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.fake.FakeFavouriteGamesRepository
import com.simovic.simplegaming.fake.FakeGameDetailRepository
import com.simovic.simplegaming.fake.FakeReelGamesRepository
import com.simovic.simplegaming.fake.GAME_ID_1
import com.simovic.simplegaming.fake.GAME_ID_2
import com.simovic.simplegaming.fake.GAME_ID_3
import com.simovic.simplegaming.fake.GAME_NAME
import com.simovic.simplegaming.fake.PAGE_SIZE
import com.simovic.simplegaming.fake.PREFETCH_THRESHOLD
import com.simovic.simplegaming.fake.VALID_USER_ID
import com.simovic.simplegaming.fake.aReelGame
import com.simovic.simplegaming.feature.games.domain.model.FavouriteGame
import com.simovic.simplegaming.feature.games.domain.model.UserConfig
import com.simovic.simplegaming.feature.games.domain.usecase.AddFavouriteGameUseCase
import com.simovic.simplegaming.feature.games.domain.usecase.GetFavouriteGameIdsUseCase
import com.simovic.simplegaming.feature.games.domain.usecase.GetFavouriteGamesUseCase
import com.simovic.simplegaming.feature.games.domain.usecase.RemoveFavouriteGameUseCase
import com.simovic.simplegaming.feature.reels.domain.usecase.GetReelGameIdsUseCase
import com.simovic.simplegaming.feature.reels.domain.usecase.GetReelGameUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldContain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReelsViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var reelsRepository: FakeReelGamesRepository
    private lateinit var gameDetailRepository: FakeGameDetailRepository
    private lateinit var favouriteGamesRepository: FakeFavouriteGamesRepository

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        reelsRepository = FakeReelGamesRepository()
        gameDetailRepository = FakeGameDetailRepository()
        favouriteGamesRepository = FakeFavouriteGamesRepository()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): ReelsViewModel {
        val userConfig = UserConfig(VALID_USER_ID)
        return ReelsViewModel(
            getReelGameIds = GetReelGameIdsUseCase(reelsRepository),
            getFavouriteGameIds = GetFavouriteGameIdsUseCase(favouriteGamesRepository, userConfig),
            getReelGame = GetReelGameUseCase(gameDetailRepository),
            getFavouriteGames = GetFavouriteGamesUseCase(favouriteGamesRepository, userConfig),
            addFavouriteGame = AddFavouriteGameUseCase(favouriteGamesRepository, userConfig),
            removeFavouriteGame = RemoveFavouriteGameUseCase(favouriteGamesRepository, userConfig),
        )
    }

    @Test
    fun `initial state is Loading`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiStateFlow.value shouldBeInstanceOf ReelsUiState.Loading::class
    }

    @Test
    fun `games fail to load - screen shows an error`() = runTest {
        reelsRepository.givenPages(Result.Failure())
        val viewModel = createViewModel()

        advanceUntilIdle()

        viewModel.uiStateFlow.value shouldBeInstanceOf ReelsUiState.Error::class
    }

    @Test
    fun `games load successfully - screen shows one page per game`() = runTest {
        reelsRepository.givenPages(Result.Success(listOf(GAME_ID_1, GAME_ID_2)))
        val viewModel = createViewModel()

        advanceUntilIdle()

        val state = viewModel.uiStateFlow.value as ReelsUiState.Content
        state.pages.size shouldBeEqualTo 2
    }

    @Test
    fun `already saved games do not appear in the feed`() = runTest {
        favouriteGamesRepository.favouriteGameIdsResult = Result.Success(setOf(GAME_ID_1))
        reelsRepository.givenPages(Result.Success(listOf(GAME_ID_1, GAME_ID_2, GAME_ID_3)))
        val viewModel = createViewModel()

        advanceUntilIdle()

        val state = viewModel.uiStateFlow.value as ReelsUiState.Content
        state.pages.size shouldBeEqualTo 2
    }

    @Test
    fun `game details load successfully - page shows the game`() = runTest {
        val game = aReelGame(GAME_ID_1)
        reelsRepository.givenPages(Result.Success(listOf(GAME_ID_1)))
        gameDetailRepository.givenGame(GAME_ID_1, Result.Success(game))
        val viewModel = createViewModel()

        advanceUntilIdle()

        val state = viewModel.uiStateFlow.value as ReelsUiState.Content
        state.pages[0] shouldBeEqualTo ReelPageState.Loaded(game)
    }

    @Test
    fun `game details fail to load - page shows an error`() = runTest {
        reelsRepository.givenPages(Result.Success(listOf(GAME_ID_1)))
        gameDetailRepository.givenGame(GAME_ID_1, Result.Failure())
        val viewModel = createViewModel()

        advanceUntilIdle()

        val state = viewModel.uiStateFlow.value as ReelsUiState.Content
        state.pages[0] shouldBeInstanceOf ReelPageState.Error::class
    }

    @Test
    fun `scrolling near the end loads more games`() = runTest {
        val firstPageIds = List(PREFETCH_THRESHOLD + 1) { "id-${it + 1}" }
        reelsRepository.givenPages(
            Result.Success(firstPageIds),
            Result.Success(listOf(GAME_ID_3)),
        )
        val viewModel = createViewModel()
        advanceUntilIdle()

        // index 1 >= (PREFETCH_THRESHOLD + 1) - PREFETCH_THRESHOLD = 1, triggers next page load
        viewModel.onPageVisible(1)
        advanceUntilIdle()

        val state = viewModel.uiStateFlow.value as ReelsUiState.Content
        state.pages.size shouldBeEqualTo firstPageIds.size + 1
    }

    @Test
    fun `no more games available - reaching the end does not fetch again`() = runTest {
        reelsRepository.givenPages(
            Result.Success(listOf(GAME_ID_1)),
            Result.Success(emptyList()), // signals end of data
        )
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onPageVisible(0) // triggers loadNextPage, gets empty → hasMoreIds = false
        advanceUntilIdle()
        val callCountAfterFirstTrigger = reelsRepository.callCount

        viewModel.onPageVisible(0) // tries again but hasMoreIds is false → no fetch
        advanceUntilIdle()

        reelsRepository.callCount shouldBeEqualTo callCountAfterFirstTrigger
    }

    @Test
    fun `scrolling quickly does not trigger duplicate fetches`() = runTest {
        val firstPageIds = List(PREFETCH_THRESHOLD + 1) { "id-${it + 1}" }
        reelsRepository.givenPages(
            Result.Success(firstPageIds),
            Result.Success(listOf(GAME_ID_3)),
        )
        val viewModel = createViewModel()
        advanceUntilIdle()

        // Trigger loadNextPage twice before coroutines run
        viewModel.onPageVisible(1)
        viewModel.onPageVisible(1)
        advanceUntilIdle()

        // 1 initial load + 1 next page = 2 total, not 3
        reelsRepository.callCount shouldBeEqualTo 2
    }

    @Test
    fun `toggleFavourite when game is in favourites - removes it`() = runTest {
        val game = aReelGame(GAME_ID_1)
        reelsRepository.givenPages(Result.Success(listOf(GAME_ID_1)))
        gameDetailRepository.givenGame(GAME_ID_1, Result.Success(game))
        val viewModel = createViewModel()
        advanceUntilIdle()

        favouriteGamesRepository.emit(
            Result.Success(listOf(FavouriteGame(gameId = GAME_ID_1, name = GAME_NAME, imageUrl = "", addedAt = 0L))),
        )
        advanceUntilIdle()

        viewModel.toggleFavourite(game)
        advanceUntilIdle()

        favouriteGamesRepository.removedGameIds shouldContain GAME_ID_1
    }

    @Test
    fun `toggleFavourite when game is not in favourites - adds it`() = runTest {
        val game = aReelGame(GAME_ID_1)
        reelsRepository.givenPages(Result.Success(listOf(GAME_ID_1)))
        gameDetailRepository.givenGame(GAME_ID_1, Result.Success(game))
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.toggleFavourite(game)
        advanceUntilIdle()

        favouriteGamesRepository.addedGames.map { it.id } shouldContain GAME_ID_1
    }

    @Test
    fun `saving a game elsewhere updates the heart icon in the feed`() = runTest {
        reelsRepository.givenPages(Result.Success(listOf(GAME_ID_1)))
        val viewModel = createViewModel()
        advanceUntilIdle()

        favouriteGamesRepository.emit(
            Result.Success(listOf(FavouriteGame(gameId = GAME_ID_1, name = GAME_NAME, imageUrl = "", addedAt = 0L))),
        )
        advanceUntilIdle()

        val state = viewModel.uiStateFlow.value as ReelsUiState.Content
        state.favouriteIds shouldBeEqualTo setOf(GAME_ID_1)
    }
}
