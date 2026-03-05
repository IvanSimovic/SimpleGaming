package com.simovic.simplegaming.feature.birthday

import com.simovic.simplegaming.feature.birthday.data.dataModule
import com.simovic.simplegaming.feature.birthday.domain.domainModule
import com.simovic.simplegaming.feature.birthday.presentation.presentationModule

val featureBirthDayModules =
    listOf(
        presentationModule,
        domainModule,
        dataModule,
    )
