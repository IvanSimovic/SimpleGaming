package com.simovic.simplegaming.feature.games.data.datasource.api.service

import com.simovic.simplegaming.base.data.retrofit.apiresult.ApiResult
import com.simovic.simplegaming.feature.games.data.datasource.api.response.SearchGamesResponse
import retrofit2.http.GET
import retrofit2.http.Query

internal interface GamesApiService {
    @GET("games")
    suspend fun searchGames(
        @Query("search") query: String,
        @Query("page_size") pageSize: Int = 10,
        @Query("search_precise") searchPrecise: Boolean = true,
    ): ApiResult<SearchGamesResponse>
}
