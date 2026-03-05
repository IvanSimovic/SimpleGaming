package com.simovic.simplegaming.feature.birthday.presentation

import com.simovic.simplegaming.feature.birthday.presentation.screen.birthdayadd.BirthDayAddViewModel
import com.simovic.simplegaming.feature.birthday.presentation.screen.birthdaylist.BirthDayViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val presentationModule =
    module {

        viewModelOf(::BirthDayViewModel)
        viewModelOf(::BirthDayAddViewModel)
    }
