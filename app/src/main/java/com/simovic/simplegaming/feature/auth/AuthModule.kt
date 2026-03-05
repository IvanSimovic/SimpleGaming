package com.simovic.simplegaming.feature.auth

import com.simovic.simplegaming.feature.auth.data.dataModule
import com.simovic.simplegaming.feature.auth.domain.domainModule
import com.simovic.simplegaming.feature.auth.presentation.presentationModule

val featureAuthModules =
    listOf(
        dataModule,
        domainModule,
        presentationModule,
    )
