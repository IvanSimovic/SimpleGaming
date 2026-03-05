package com.simovic.simplegaming.feature.birthday.domain.repository

import com.simovic.simplegaming.base.domain.result.Result
import com.simovic.simplegaming.feature.birthday.domain.model.BirthDay
import com.simovic.simplegaming.feature.birthday.domain.model.CreateBirthDay

internal interface BirthDayRepo {
    suspend fun get(): Result<List<BirthDay>>

    suspend fun add(birthday: CreateBirthDay): Result<Unit>

    suspend fun remove(id: Long): Result<Unit>
}
