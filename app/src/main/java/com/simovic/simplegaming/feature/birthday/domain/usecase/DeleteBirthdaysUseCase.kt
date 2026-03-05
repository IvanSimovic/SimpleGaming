package com.simovic.simplegaming.feature.birthday.domain.usecase

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.birthday.domain.repository.BirthDayRepo

internal class DeleteBirthdaysUseCase(
    private val repo: BirthDayRepo,
) {
    suspend operator fun invoke(ids: List<Long>): Result<Unit> {
        ids.forEach { id ->
            val result = repo.remove(id)
            if (result is Result.Failure) return result
        }
        return Result.Success(Unit)
    }
}
