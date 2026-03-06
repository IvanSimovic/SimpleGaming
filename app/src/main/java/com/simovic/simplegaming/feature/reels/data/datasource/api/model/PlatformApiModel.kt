package com.simovic.simplegaming.feature.reels.data.datasource.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlatformApiModel(
    @SerialName("name") val name: String,
)

@Serializable
data class PlatformWrapperApiModel(
    @SerialName("platform") val platform: PlatformApiModel,
)
