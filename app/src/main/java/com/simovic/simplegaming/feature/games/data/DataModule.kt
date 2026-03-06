package com.simovic.simplegaming.feature.games.data

import com.google.firebase.firestore.FirebaseFirestore
import com.simovic.simplegaming.feature.games.data.datasource.api.service.GamesApiService
import com.simovic.simplegaming.feature.games.data.datasource.firestore.FavouriteGamesFirestore
import com.simovic.simplegaming.feature.games.data.mapper.FavouriteGameMapper
import com.simovic.simplegaming.feature.games.data.mapper.GameMapper
import com.simovic.simplegaming.feature.games.data.repository.FavouriteGamesRepositoryImpl
import com.simovic.simplegaming.feature.games.data.repository.GameSearchRepositoryImpl
import com.simovic.simplegaming.feature.games.domain.repository.FavouriteGamesRepository
import com.simovic.simplegaming.feature.games.domain.repository.GameSearchRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

internal val dataModule =
    module {
        single { FirebaseFirestore.getInstance() }

        singleOf(::FavouriteGamesFirestore)

        single { get<Retrofit>(named("rawgRetrofit")).create(GamesApiService::class.java) }

        singleOf(::GameMapper)
        singleOf(::FavouriteGameMapper)

        singleOf(::FavouriteGamesRepositoryImpl) { bind<FavouriteGamesRepository>() }
        singleOf(::GameSearchRepositoryImpl) { bind<GameSearchRepository>() }
    }
