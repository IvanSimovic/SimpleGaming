package com.simovic.simplegaming.feature.games.data.datasource.api.response

import com.simovic.simplegaming.feature.games.data.datasource.api.model.GameApiModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SearchGamesResponse(
    @SerialName("results") val results: List<GameApiModel>,
)
