package com.simovic.simplegaming.feature.auth.domain

import com.simovic.simplegaming.base.domain.AuthStateProvider
import com.simovic.simplegaming.feature.auth.domain.usecase.GetCurrentUserIdUseCase
import com.simovic.simplegaming.feature.auth.domain.usecase.ObserveAuthStateUseCase
import com.simovic.simplegaming.feature.auth.domain.usecase.SignInUseCase
import com.simovic.simplegaming.feature.auth.domain.usecase.SignOutUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val domainModule =
    module {

        singleOf(::ObserveAuthStateUseCase) bind AuthStateProvider::class

        singleOf(::SignInUseCase)

        singleOf(::SignOutUseCase)

        singleOf(::GetCurrentUserIdUseCase)
    }
