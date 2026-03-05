package com.simovic.simplegaming.base.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationScreens {
    @Serializable object AlbumList : NavigationScreens

    @Serializable data class AlbumDetail(
        val artistName: String,
        val albumName: String,
        val albumMbId: String? = null,
    ) : NavigationScreens

    @Serializable object LiveFeed : NavigationScreens

    @Serializable data class LiveFeedDetail(
        val guid: String,
        val title: String,
        val link: String,
        val pubDate: String,
        val description: String,
    ) : NavigationScreens

    @Serializable object BirthDayList : NavigationScreens

    @Serializable object BirthDayAdd : NavigationScreens
}
