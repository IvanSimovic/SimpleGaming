package com.simovic.simplegaming.feature.album

import com.simovic.simplegaming.feature.album.data.dataModule
import com.simovic.simplegaming.feature.album.domain.domainModule
import com.simovic.simplegaming.feature.album.presentation.presentationModule

val featureAlbumModules =
    listOf(
        presentationModule,
        domainModule,
        dataModule,
    )
