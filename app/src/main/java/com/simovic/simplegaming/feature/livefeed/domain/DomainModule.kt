package com.simovic.simplegaming.feature.livefeed.domain

import com.simovic.simplegaming.feature.livefeed.domain.usecase.GetLiveFeedUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val domainModule =
    module {

        singleOf(::GetLiveFeedUseCase)
    }
