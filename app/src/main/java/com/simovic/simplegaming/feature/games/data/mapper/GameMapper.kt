package com.simovic.simplegaming.feature.games.data.mapper

import com.simovic.simplegaming.feature.games.data.datasource.api.model.GameApiModel
import com.simovic.simplegaming.feature.games.domain.model.Game

internal class GameMapper {
    fun toDomain(model: GameApiModel): Game =
        Game(
            id = model.id.toString(),
            name = model.name,
            imageUrl = model.backgroundImage ?: "",
        )
}
