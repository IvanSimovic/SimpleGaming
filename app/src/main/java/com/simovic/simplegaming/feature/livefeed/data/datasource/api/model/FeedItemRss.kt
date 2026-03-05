@file:Suppress("DataClassShouldBeImmutable")

package com.simovic.simplegaming.feature.livefeed.data.datasource.api.model

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "rss")
internal data class RssFeed(
    @field:Element(name = "channel")
    var channel: Channel = Channel(),
)

@Xml(name = "channel")
internal data class Channel(
    @field:Element(name = "item")
    var items: MutableList<FeedItemRss> = mutableListOf(),
)

@Xml(name = "item")
internal data class FeedItemRss(
    @field:PropertyElement(name = "title")
    var title: String = "",
    @field:PropertyElement(name = "link")
    var link: String = "",
    @field:PropertyElement(name = "pubDate")
    var pubDate: String = "",
    @field:PropertyElement(name = "description")
    var description: String = "",
    @field:PropertyElement(name = "guid")
    var guid: String = "",
)
