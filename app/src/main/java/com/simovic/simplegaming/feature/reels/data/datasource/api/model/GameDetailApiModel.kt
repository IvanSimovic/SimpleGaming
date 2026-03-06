package com.simovic.simplegaming.feature.reels.data.datasource.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameDetailApiModel(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("description_raw") val descriptionRaw: String = "",
    @SerialName("background_image") val backgroundImage: String? = null,
    @SerialName("metacritic") val metacritic: Int? = null,
    @SerialName("rating") val rating: Float = 0f,
    @SerialName("playtime") val playtime: Int = 0,
    @SerialName("genres") val genres: List<GenreApiModel> = emptyList(),
    @SerialName("platforms") val platforms: List<PlatformWrapperApiModel> = emptyList(),
)
