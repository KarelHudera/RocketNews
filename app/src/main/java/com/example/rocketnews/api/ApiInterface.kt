package com.example.rocketnews.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET(value = "apod")
    suspend fun getResponseNasa(
        @Query("api_key") apiKey: String
    ): Response<ResponseNasa>
}