package com.simovic.simplegaming.feature.livefeed.data.mapper

import com.simovic.simplegaming.feature.livefeed.data.datasource.api.model.FeedItemRss
import com.simovic.simplegaming.feature.livefeed.data.datasource.api.model.RssFeed
import com.simovic.simplegaming.feature.livefeed.domain.model.FeedItem

internal class LiveFeedMapper {
    fun rssToDomain(rssModel: FeedItemRss): FeedItem =
        FeedItem(
            guid = rssModel.guid,
            title = rssModel.title,
            link = rssModel.link,
            pubDate = rssModel.pubDate,
            description = rssModel.description,
        )

    fun rssToDomain(feed: RssFeed): List<FeedItem>? =
        feed.channel?.items?.map { rssModel ->
            FeedItem(
                guid = rssModel.guid,
                title = rssModel.title,
                link = rssModel.link,
                pubDate = rssModel.pubDate,
                description = rssModel.description,
            )
        }
}
