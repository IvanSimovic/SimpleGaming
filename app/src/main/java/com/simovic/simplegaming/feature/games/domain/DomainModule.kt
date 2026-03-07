package com.simovic.simplegaming.feature.games.domain

import com.simovic.simplegaming.feature.auth.domain.usecase.GetCurrentUserIdUseCase
import com.simovic.simplegaming.feature.games.domain.model.UserConfig
import com.simovic.simplegaming.feature.games.domain.usecase.AddFavouriteGameUseCase
import com.simovic.simplegaming.feature.games.domain.usecase.GetFavouriteGameIdsUseCase
import com.simovic.simplegaming.feature.games.domain.usecase.GetFavouriteGamesUseCase
import com.simovic.simplegaming.feature.games.domain.usecase.RemoveFavouriteGameUseCase
import com.simovic.simplegaming.feature.games.domain.usecase.SearchGamesUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val domainModule =
    module {
        single { UserConfig(userId = get<GetCurrentUserIdUseCase>()() ?: "") }

        singleOf(::GetFavouriteGamesUseCase)
        singleOf(::GetFavouriteGameIdsUseCase)
        singleOf(::AddFavouriteGameUseCase)
        singleOf(::RemoveFavouriteGameUseCase)
        singleOf(::SearchGamesUseCase)
    }
