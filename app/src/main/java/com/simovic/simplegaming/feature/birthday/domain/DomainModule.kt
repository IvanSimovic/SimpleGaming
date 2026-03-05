package com.simovic.simplegaming.feature.birthday.domain

import com.simovic.simplegaming.feature.birthday.domain.usecase.AddBirthdayUseCase
import com.simovic.simplegaming.feature.birthday.domain.usecase.DeleteBirthdaysUseCase
import com.simovic.simplegaming.feature.birthday.domain.usecase.GetBirthdaysUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val domainModule =
    module {

        singleOf(::GetBirthdaysUseCase)

        singleOf(::AddBirthdayUseCase)

        singleOf(::DeleteBirthdaysUseCase)
    }
