package com.example.rocketnews.apiSpaceX

import com.example.rocketnews.apiNasa.ResponseNasa
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiSpaceXInterface {
    @GET(value = "launches")
    suspend fun getResponseSpaceX(
    ): Response<List<ResponseSpaceXItem>>
}