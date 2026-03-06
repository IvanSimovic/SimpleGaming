package com.simovic.simplegaming.feature.reels.domain.repository

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.reels.domain.model.ReelGame

interface GameDetailRepository {
    suspend fun getReelGame(id: String): Result<ReelGame>
}
