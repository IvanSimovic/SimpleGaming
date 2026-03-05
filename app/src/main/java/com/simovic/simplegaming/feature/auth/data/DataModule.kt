package com.simovic.simplegaming.feature.auth.data

import com.google.firebase.auth.FirebaseAuth
import com.simovic.simplegaming.feature.auth.data.datasource.firebase.FirebaseAuthDataSource
import com.simovic.simplegaming.feature.auth.data.repository.AuthRepositoryImpl
import com.simovic.simplegaming.feature.auth.domain.repository.AuthRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val dataModule =
    module {

        single { FirebaseAuth.getInstance() }

        singleOf(::FirebaseAuthDataSource)

        singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }
    }
