package com.simovic.simplegaming.feature.auth.presentation

import com.simovic.simplegaming.feature.auth.presentation.screen.login.LoginViewModel
import com.simovic.simplegaming.feature.auth.presentation.screen.temp.TempViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val presentationModule =
    module {

        viewModelOf(::LoginViewModel)

        viewModelOf(::TempViewModel)
    }
