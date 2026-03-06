package com.simovic.simplegaming.feature.reels.domain

import com.simovic.simplegaming.feature.reels.domain.usecase.GetReelGameIdsUseCase
import com.simovic.simplegaming.feature.reels.domain.usecase.GetReelGameUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val domainModule =
    module {
        singleOf(::GetReelGameIdsUseCase)
        singleOf(::GetReelGameUseCase)
    }
