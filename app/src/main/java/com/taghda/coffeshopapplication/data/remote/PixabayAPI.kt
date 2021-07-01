package com.taghda.coffeshopapplication.data.remote

import com.taghda.coffeshopapplication.BuildConfig
import com.taghda.coffeshopapplication.data.remote.responses.ImageResponse
import com.taghda.coffeshopapplication.data.remote.responses.ImageResponsee
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayAPI {

/*    @GET("/api/")
    suspend fun searchForImage(
        @Query("q") searchQuery: String,
        @Query("key") apiKey: String = BuildConfig.API_KEY
    ): Response<ImageResponse>
*/
    @GET("photos?")
    suspend fun searchForImage(
        @Query("query") searchQuery: String,
        @Query("client_id") apiKey: String = BuildConfig.API_KEY
    ): Response<ImageResponsee>


}