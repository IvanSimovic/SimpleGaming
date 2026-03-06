package com.simovic.simplegaming.feature.games.data.mapper

import com.simovic.simplegaming.feature.games.data.datasource.firestore.model.FavouriteGameFirestoreModel
import com.simovic.simplegaming.feature.games.domain.model.FavouriteGame

internal class FavouriteGameMapper {
    fun toDomain(model: FavouriteGameFirestoreModel): FavouriteGame =
        FavouriteGame(
            gameId = model.gameId,
            name = model.name,
            imageUrl = model.imageUrl,
            addedAt = model.addedAt,
        )
}
