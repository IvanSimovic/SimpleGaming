package com.simovic.simplegaming.feature.livefeed.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class FeedItem(
    val guid: String,
    val title: String,
    val link: String,
    val pubDate: String,
    val description: String,
)
