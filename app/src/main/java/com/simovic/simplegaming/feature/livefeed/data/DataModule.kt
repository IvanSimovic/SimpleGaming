package com.simovic.simplegaming.feature.livefeed.data

import com.simovic.simplegaming.feature.livefeed.data.datasource.api.service.LiveFeedService
import com.simovic.simplegaming.feature.livefeed.data.mapper.LiveFeedMapper
import com.simovic.simplegaming.feature.livefeed.data.repository.LiveFeedRepoImpl
import com.simovic.simplegaming.feature.livefeed.domain.repository.LiveFeedRepo
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val dataModule =
    module {

        single {
            get<Retrofit>(named("xmlRetrofit")).create(LiveFeedService::class.java)
        }

        singleOf(::LiveFeedMapper)
        singleOf(::LiveFeedRepoImpl) { bind<LiveFeedRepo>() }
    }
