package com.simovic.simplegaming.base.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationScreens {
    @Serializable object FavouriteGames : NavigationScreens

    @Serializable object AddGame : NavigationScreens

    @Serializable object Reels : NavigationScreens
}
