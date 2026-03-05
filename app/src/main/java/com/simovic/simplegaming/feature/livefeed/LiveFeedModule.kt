package com.simovic.simplegaming.feature.livefeed

import com.simovic.simplegaming.feature.livefeed.data.dataModule
import com.simovic.simplegaming.feature.livefeed.domain.domainModule
import com.simovic.simplegaming.feature.livefeed.presentation.presentationModule

val featureLiveFeedModules =
    listOf(
        dataModule,
        domainModule,
        presentationModule,
    )
