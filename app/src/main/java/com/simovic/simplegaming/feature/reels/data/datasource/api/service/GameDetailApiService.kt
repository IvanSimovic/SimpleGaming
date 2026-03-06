package com.simovic.simplegaming.feature.reels.data.datasource.api.service

import com.simovic.simplegaming.base.data.retrofit.apiresult.ApiResult
import com.simovic.simplegaming.feature.reels.data.datasource.api.model.GameDetailApiModel
import com.simovic.simplegaming.feature.reels.data.datasource.api.response.ScreenshotsResponse
import retrofit2.http.GET
import retrofit2.http.Path

internal interface GameDetailApiService {
    @GET("games/{id}")
    suspend fun getGameDetail(
        @Path("id") id: String,
    ): ApiResult<GameDetailApiModel>

    @GET("games/{id}/screenshots")
    suspend fun getGameScreenshots(
        @Path("id") id: String,
    ): ApiResult<ScreenshotsResponse>
}
