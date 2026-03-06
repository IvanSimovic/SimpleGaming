package com.simovic.simplegaming.feature.games.domain.model

data class FavouriteGame(
    val gameId: String,
    val name: String,
    val imageUrl: String,
    val addedAt: Long, // epoch millis
)
