package com.simovic.simplegaming.feature.birthday.domain.usecase

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.birthday.domain.model.BirthDay
import com.simovic.simplegaming.feature.birthday.domain.repository.BirthDayRepo

internal class GetBirthdaysUseCase(
    private val repo: BirthDayRepo,
) {
    suspend operator fun invoke(): Result<List<BirthDay>> = repo.get()
}
