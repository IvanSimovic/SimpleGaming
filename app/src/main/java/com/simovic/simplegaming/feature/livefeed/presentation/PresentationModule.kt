package com.simovic.simplegaming.feature.livefeed.presentation

import com.simovic.simplegaming.feature.livefeed.presentation.screen.livefeed.LiveFeedViewModel
import com.simovic.simplegaming.feature.livefeed.presentation.screen.livefeeddetails.LiveFeedDetailsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val presentationModule =
    module {

        // Feed
        viewModelOf(::LiveFeedViewModel)
        viewModelOf(::LiveFeedDetailsViewModel)
    }
