package com.example.rocketnews.apiSpaceX

import retrofit2.Response
import retrofit2.http.GET

interface ApiSpaceXInterface {
    @GET(value = "launches")
    suspend fun getResponseSpaceX(
    ): Response<List<ResponseSpaceXItem>>
}