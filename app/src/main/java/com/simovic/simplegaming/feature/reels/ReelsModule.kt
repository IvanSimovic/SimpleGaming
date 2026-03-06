package com.simovic.simplegaming.feature.reels

import com.simovic.simplegaming.feature.reels.data.dataModule
import com.simovic.simplegaming.feature.reels.domain.domainModule
import com.simovic.simplegaming.feature.reels.presentation.presentationModule

val featureReelsModules =
    listOf(
        dataModule,
        domainModule,
        presentationModule,
    )
