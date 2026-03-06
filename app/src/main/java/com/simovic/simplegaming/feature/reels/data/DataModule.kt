package com.simovic.simplegaming.feature.reels.data

import com.simovic.simplegaming.feature.reels.data.datasource.api.service.GameDetailApiService
import com.simovic.simplegaming.feature.reels.data.datasource.firestore.ReelGamesFirestore
import com.simovic.simplegaming.feature.reels.data.mapper.ReelGameMapper
import com.simovic.simplegaming.feature.reels.data.repository.GameDetailRepositoryImpl
import com.simovic.simplegaming.feature.reels.data.repository.ReelGamesRepositoryImpl
import com.simovic.simplegaming.feature.reels.domain.repository.GameDetailRepository
import com.simovic.simplegaming.feature.reels.domain.repository.ReelGamesRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

internal val dataModule =
    module {
        single { get<Retrofit>(named("rawgRetrofit")).create(GameDetailApiService::class.java) }

        singleOf(::ReelGamesFirestore)
        singleOf(::ReelGameMapper)

        singleOf(::ReelGamesRepositoryImpl) { bind<ReelGamesRepository>() }
        singleOf(::GameDetailRepositoryImpl) { bind<GameDetailRepository>() }
    }
