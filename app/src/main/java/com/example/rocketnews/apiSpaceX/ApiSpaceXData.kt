package com.example.rocketnews.apiSpaceX

import com.example.rocketnews.apiNasa.ApiNasaInterface
import com.example.rocketnews.apiNasa.ResponseNasa
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val baseUrl = "https://api.spacexdata.com/v4/"

object ApiSpaceXData {
    private val apiInterface by lazy {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        retrofit.create(ApiSpaceXInterface::class.java)
    }

    suspend fun getResponseSpaceX(): Response<List<ResponseSpaceXItem>> {
        return apiInterface.getResponseSpaceX()
    }
}