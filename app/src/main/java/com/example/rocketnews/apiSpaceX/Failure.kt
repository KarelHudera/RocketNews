package com.example.rocketnews.apiSpaceX


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Failure(
    @Json(name = "altitude")
    val altitude: Int?,
    @Json(name = "reason")
    val reason: String?,
    @Json(name = "time")
    val time: Int?
)