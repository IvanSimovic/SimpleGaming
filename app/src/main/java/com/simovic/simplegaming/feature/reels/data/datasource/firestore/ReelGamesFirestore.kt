package com.simovic.simplegaming.feature.reels.data.datasource.firestore

import com.simovic.simplegaming.base.domain.result.Result

internal class ReelGamesFirestore {
    suspend fun getReelGameIds(): Result<List<String>> =
        Result.Success(
            listOf(
                "4427", // BioShock 2
                "638650", // Ghost of Tsushima
                "39", // Prey (2017)
                "3328", // The Witcher 3: Wild Hunt
            ),
        )
}
