package com.simovic.simplegaming.feature.reels.domain.usecase

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.reels.domain.model.ReelGame
import com.simovic.simplegaming.feature.reels.domain.repository.GameDetailRepository

class GetReelGameUseCase(
    private val repository: GameDetailRepository,
) {
    suspend operator fun invoke(id: String): Result<ReelGame> = repository.getReelGame(id)
}
