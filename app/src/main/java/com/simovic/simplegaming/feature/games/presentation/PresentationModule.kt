package com.simovic.simplegaming.feature.games.presentation

import com.simovic.simplegaming.feature.games.presentation.screen.addgame.AddGameViewModel
import com.simovic.simplegaming.feature.games.presentation.screen.favouritegames.FavouriteGamesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val presentationModule =
    module {
        viewModelOf(::FavouriteGamesViewModel)
        viewModelOf(::AddGameViewModel)
    }
