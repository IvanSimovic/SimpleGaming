package com.simovic.simplegaming.feature.reels.domain.repository

import com.simovic.simplegaming.base.domain.result.Result

interface ReelGamesRepository {
    suspend fun getNextReelGameIds(pageSize: Int): Result<List<String>>

    fun resetPaging()
}
