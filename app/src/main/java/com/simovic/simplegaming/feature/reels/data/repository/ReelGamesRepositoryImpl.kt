package com.simovic.simplegaming.feature.reels.data.repository

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.reels.data.datasource.firestore.ReelGamesFirestore
import com.simovic.simplegaming.feature.reels.domain.repository.ReelGamesRepository

internal class ReelGamesRepositoryImpl(
    private val dataSource: ReelGamesFirestore,
) : ReelGamesRepository {
    override suspend fun getReelGameIds(): Result<List<String>> = dataSource.getReelGameIds()
}
