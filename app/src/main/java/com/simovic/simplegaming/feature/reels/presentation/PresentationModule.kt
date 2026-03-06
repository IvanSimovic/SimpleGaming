package com.simovic.simplegaming.feature.reels.presentation

import com.simovic.simplegaming.feature.reels.presentation.screen.reels.ReelsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val presentationModule =
    module {
        viewModelOf(::ReelsViewModel)
    }
