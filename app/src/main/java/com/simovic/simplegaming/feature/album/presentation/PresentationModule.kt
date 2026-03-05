package com.simovic.simplegaming.feature.album.presentation

import coil.ImageLoader
import com.simovic.simplegaming.feature.album.presentation.screen.albumdetail.AlbumDetailViewModel
import com.simovic.simplegaming.feature.album.presentation.screen.albumlist.AlbumListViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val presentationModule =
    module {

        // AlbumList
        viewModelOf(::AlbumListViewModel)

        singleOf(::ImageLoader)

        // AlbumDetails
        viewModelOf(::AlbumDetailViewModel)
    }
