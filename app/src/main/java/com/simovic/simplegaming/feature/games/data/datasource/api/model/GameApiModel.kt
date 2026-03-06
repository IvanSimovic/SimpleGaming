package com.simovic.simplegaming.feature.games.data.datasource.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GameApiModel(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("background_image") val backgroundImage: String? = null,
)
