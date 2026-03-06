package com.simovic.simplegaming.feature.games.presentation.screen.favouritegames

import androidx.compose.runtime.Immutable

@Immutable
data class FavouriteGameUiModel(
    val gameId: String,
    val name: String,
    val imageUrl: String,
)
