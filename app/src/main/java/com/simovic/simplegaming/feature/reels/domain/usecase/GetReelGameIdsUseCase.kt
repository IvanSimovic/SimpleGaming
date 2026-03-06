package com.simovic.simplegaming.feature.reels.domain.usecase

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.reels.domain.repository.ReelGamesRepository

class GetReelGameIdsUseCase(
    private val repository: ReelGamesRepository,
) {
    suspend operator fun invoke(): Result<List<String>> = repository.getReelGameIds()
}
