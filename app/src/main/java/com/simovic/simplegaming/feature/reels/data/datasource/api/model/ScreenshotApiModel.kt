package com.simovic.simplegaming.feature.reels.data.datasource.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScreenshotApiModel(
    @SerialName("id") val id: Int,
    @SerialName("image") val image: String,
)
