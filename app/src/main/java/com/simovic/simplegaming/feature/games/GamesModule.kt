package com.simovic.simplegaming.feature.games

import com.simovic.simplegaming.feature.games.data.dataModule
import com.simovic.simplegaming.feature.games.domain.domainModule
import com.simovic.simplegaming.feature.games.presentation.presentationModule

val featureGamesModules =
    listOf(
        dataModule,
        domainModule,
        presentationModule,
    )
