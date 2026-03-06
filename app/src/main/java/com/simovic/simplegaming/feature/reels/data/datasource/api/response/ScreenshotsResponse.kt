package com.simovic.simplegaming.feature.reels.data.datasource.api.response

import com.simovic.simplegaming.feature.reels.data.datasource.api.model.ScreenshotApiModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScreenshotsResponse(
    @SerialName("results") val results: List<ScreenshotApiModel> = emptyList(),
)
