package com.simovic.simplegaming.feature.reels.domain.usecase

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.fake.FakeReelGamesRepository
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeEqualTo
import com.simovic.simplegaming.fake.GAME_ID_1
import com.simovic.simplegaming.fake.GAME_ID_2
import com.simovic.simplegaming.fake.GAME_ID_3
import com.simovic.simplegaming.fake.PAGE_SIZE
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetReelGameIdsUseCaseTest {
    private lateinit var repository: FakeReelGamesRepository
    private lateinit var useCase: GetReelGameIdsUseCase

    @BeforeEach
    fun setUp() {
        repository = FakeReelGamesRepository()
        useCase = GetReelGameIdsUseCase(repository)
    }

    @Test
    fun `returns all ids when no savedIds provided`() = runTest {
        repository.givenPages(Result.Success(listOf(GAME_ID_1, GAME_ID_2, GAME_ID_3)))

        val result = useCase(pageSize = PAGE_SIZE, savedIds = emptySet())

        (result as Result.Success).value shouldBeEqualTo listOf(GAME_ID_1, GAME_ID_2, GAME_ID_3)
    }

    @Test
    fun `already saved games do not appear in the results`() = runTest {
        repository.givenPages(Result.Success(listOf(GAME_ID_1, GAME_ID_2, GAME_ID_3)))

        val result = useCase(pageSize = PAGE_SIZE, savedIds = setOf(GAME_ID_1, GAME_ID_3))

        (result as Result.Success).value shouldBeEqualTo listOf(GAME_ID_2)
    }

    @Test
    fun `returns an empty list when all games are already saved`() = runTest {
        repository.givenPages(Result.Success(listOf(GAME_ID_1, GAME_ID_2)))

        val result = useCase(pageSize = PAGE_SIZE, savedIds = setOf(GAME_ID_1, GAME_ID_2))

        result shouldBeInstanceOf Result.Success::class
        (result as Result.Success).value shouldBeEqualTo emptyList()
    }

    @Test
    fun `returns an error when the data source fails`() = runTest {
        repository.givenPages(Result.Failure())

        val result = useCase(pageSize = PAGE_SIZE, savedIds = emptySet())

        result shouldBeInstanceOf Result.Failure::class
    }

    @Test
    fun `reset clears the paging cursor`() {
        useCase.reset()

        repository.resetCount shouldBeEqualTo 1
    }
}
