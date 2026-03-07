package com.simovic.simplegaming.feature.games.domain.usecase

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.fake.FakeFavouriteGamesRepository
import com.simovic.simplegaming.feature.games.domain.model.UserConfig
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeEqualTo
import com.simovic.simplegaming.fake.GAME_ID_1
import com.simovic.simplegaming.fake.GAME_ID_2
import com.simovic.simplegaming.fake.GAME_ID_3
import com.simovic.simplegaming.fake.VALID_USER_ID
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetFavouriteGameIdsUseCaseTest {
    private lateinit var repository: FakeFavouriteGamesRepository
    private lateinit var useCase: GetFavouriteGameIdsUseCase

    @BeforeEach
    fun setUp() {
        repository = FakeFavouriteGamesRepository()
        useCase = GetFavouriteGameIdsUseCase(repository, UserConfig(userId = VALID_USER_ID))
    }

    @Test
    fun `returns the user's saved game ids`() = runTest {
        repository.favouriteGameIdsResult = Result.Success(setOf(GAME_ID_1, GAME_ID_2, GAME_ID_3))

        val result = useCase()

        result shouldBeInstanceOf Result.Success::class
        (result as Result.Success).value shouldBeEqualTo setOf(GAME_ID_1, GAME_ID_2, GAME_ID_3)
    }

    @Test
    fun `returns an error when the data source fails`() = runTest {
        repository.favouriteGameIdsResult = Result.Failure()

        val result = useCase()

        result shouldBeInstanceOf Result.Failure::class
    }
}
