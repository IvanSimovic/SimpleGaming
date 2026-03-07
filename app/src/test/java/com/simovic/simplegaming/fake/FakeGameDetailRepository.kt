package com.simovic.simplegaming.fake

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.reels.domain.model.ReelGame
import com.simovic.simplegaming.feature.reels.domain.repository.GameDetailRepository

class FakeGameDetailRepository : GameDetailRepository {
    private val results = mutableMapOf<String, Result<ReelGame>>()
    var defaultResult: Result<ReelGame> = Result.Success(aReelGame())

    fun givenGame(id: String, result: Result<ReelGame>) {
        results[id] = result
    }

    override suspend fun getReelGame(id: String): Result<ReelGame> = results[id] ?: defaultResult
}
