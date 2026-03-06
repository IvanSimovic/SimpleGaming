package com.simovic.simplegaming.feature.reels.domain.model

data class ReelGame(
    val id: String,
    val name: String,
    val description: String,
    val heroImage: String,
    val metacritic: Int?,
    val rating: Float,
    val genres: List<String>,
    val platforms: List<String>,
    val playtime: Int,
    val screenshots: List<String>,
)
