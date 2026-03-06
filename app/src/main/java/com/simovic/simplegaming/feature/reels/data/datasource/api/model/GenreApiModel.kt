package com.simovic.simplegaming.feature.reels.data.datasource.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreApiModel(
    @SerialName("name") val name: String,
)
