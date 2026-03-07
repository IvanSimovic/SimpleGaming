package com.simovic.simplegaming.fake

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.reels.domain.repository.ReelGamesRepository

class FakeReelGamesRepository : ReelGamesRepository {
    private val pages = ArrayDeque<Result<List<String>>>()
    var resetCount = 0
    var callCount = 0

    fun givenPages(vararg results: Result<List<String>>) {
        pages.clear()
        pages.addAll(results)
    }

    override suspend fun getNextReelGameIds(pageSize: Int): Result<List<String>> {
        callCount++
        return if (pages.isNotEmpty()) pages.removeFirst() else Result.Success(emptyList())
    }

    override fun resetPaging() {
        resetCount++
    }
}
