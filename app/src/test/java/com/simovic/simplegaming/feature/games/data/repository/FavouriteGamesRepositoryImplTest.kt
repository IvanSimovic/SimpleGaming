package com.simovic.simplegaming.feature.games.data.repository

import app.cash.turbine.test
import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.fake.GAME_ID_1
import com.simovic.simplegaming.fake.GAME_NAME
import com.simovic.simplegaming.fake.VALID_USER_ID
import com.simovic.simplegaming.fake.aGame
import com.simovic.simplegaming.feature.games.data.datasource.firestore.FavouriteGamesFirestore
import com.simovic.simplegaming.feature.games.data.datasource.firestore.model.FavouriteGameFirestoreModel
import com.simovic.simplegaming.feature.games.data.mapper.FavouriteGameMapper
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test

class FavouriteGamesRepositoryImplTest {
    private val dataSource = mockk<FavouriteGamesFirestore>()
    private val repository = FavouriteGamesRepositoryImpl(dataSource, FavouriteGameMapper())

    @Test
    fun `getFavouriteGameIds success - returns the saved game ids`() = runTest {
        coEvery { dataSource.getFavouriteGameIds(VALID_USER_ID) } returns setOf(GAME_ID_1)

        val result = repository.getFavouriteGameIds(VALID_USER_ID)

        (result as Result.Success).value shouldBeEqualTo setOf(GAME_ID_1)
    }

    @Test
    fun `getFavouriteGameIds network error - returns a failure`() = runTest {
        coEvery { dataSource.getFavouriteGameIds(any()) } throws Exception("network error")

        val result = repository.getFavouriteGameIds(VALID_USER_ID)

        result shouldBeInstanceOf Result.Failure::class
    }

    @Test
    fun `addFavouriteGame success - completes without error`() = runTest {
        coEvery { dataSource.addFavouriteGame(any(), any()) } just Runs

        val result = repository.addFavouriteGame(VALID_USER_ID, aGame())

        result shouldBeEqualTo Result.Success(Unit)
    }

    @Test
    fun `addFavouriteGame network error - returns a failure`() = runTest {
        coEvery { dataSource.addFavouriteGame(any(), any()) } throws Exception()

        val result = repository.addFavouriteGame(VALID_USER_ID, aGame())

        result shouldBeInstanceOf Result.Failure::class
    }

    @Test
    fun `removeFavouriteGame success - completes without error`() = runTest {
        coEvery { dataSource.removeFavouriteGame(any(), any()) } just Runs

        val result = repository.removeFavouriteGame(VALID_USER_ID, GAME_ID_1)

        result shouldBeEqualTo Result.Success(Unit)
    }

    @Test
    fun `removeFavouriteGame network error - returns a failure`() = runTest {
        coEvery { dataSource.removeFavouriteGame(any(), any()) } throws Exception()

        val result = repository.removeFavouriteGame(VALID_USER_ID, GAME_ID_1)

        result shouldBeInstanceOf Result.Failure::class
    }

    @Test
    fun `getFavouriteGames success - returns games with correct id and name`() = runTest {
        val firestoreModel = FavouriteGameFirestoreModel(
            gameId = GAME_ID_1,
            name = GAME_NAME,
            imageUrl = "",
            addedAt = 0L,
        )
        every { dataSource.observeFavouriteGames(VALID_USER_ID) } returns flowOf(Result.Success(listOf(firestoreModel)))

        repository.getFavouriteGames(VALID_USER_ID).test {
            val result = awaitItem() as Result.Success
            result.value.first().gameId shouldBeEqualTo GAME_ID_1
            result.value.first().name shouldBeEqualTo GAME_NAME
            awaitComplete()
        }
    }

    @Test
    fun `getFavouriteGames network error - returns a failure`() = runTest {
        every { dataSource.observeFavouriteGames(any()) } returns flowOf(Result.Failure())

        repository.getFavouriteGames(VALID_USER_ID).test {
            awaitItem() shouldBeInstanceOf Result.Failure::class
            awaitComplete()
        }
    }
}
