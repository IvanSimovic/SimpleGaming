package com.simovic.simplegaming.feature.album.domain.model

import com.simovic.simplegaming.feature.album.domain.enum.ImageSize

internal data class Image(
    val url: String,
    val size: ImageSize,
)
