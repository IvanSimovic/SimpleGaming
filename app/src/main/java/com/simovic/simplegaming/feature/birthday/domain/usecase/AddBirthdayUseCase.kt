package com.simovic.simplegaming.feature.birthday.domain.usecase

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.birthday.domain.model.CreateBirthDay
import com.simovic.simplegaming.feature.birthday.domain.repository.BirthDayRepo

internal class AddBirthdayUseCase(
    private val repo: BirthDayRepo,
) {
    suspend operator fun invoke(birthday: CreateBirthDay): Result<Unit> = repo.add(birthday)
}
