package com.simovic.simplegaming.feature.games.data.datasource.firestore.model

internal data class FavouriteGameFirestoreModel(
    val gameId: String,
    val name: String,
    val imageUrl: String,
    val addedAt: Long, // epoch millis
)
