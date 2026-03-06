package com.simovic.simplegaming.feature.reels.data.mapper

import com.simovic.simplegaming.feature.reels.data.datasource.api.model.GameDetailApiModel
import com.simovic.simplegaming.feature.reels.domain.model.ReelGame

internal class ReelGameMapper {
    fun toDomain(
        detail: GameDetailApiModel,
        screenshots: List<String>,
    ): ReelGame =
        ReelGame(
            id = detail.id.toString(),
            name = detail.name,
            description = detail.descriptionRaw,
            heroImage = detail.backgroundImage ?: "",
            metacritic = detail.metacritic,
            rating = detail.rating,
            genres = detail.genres.map { it.name },
            platforms = detail.platforms.map { it.platform.name },
            playtime = detail.playtime,
            screenshots = screenshots,
        )
}
