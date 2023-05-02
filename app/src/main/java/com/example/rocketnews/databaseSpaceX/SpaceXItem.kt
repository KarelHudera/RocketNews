package com.example.rocketnews.databaseSpaceX

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "spacex_items")
data class SpaceXItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String?,
    val imageUrl: String?,
    val dateUtc: String?,
    val youtubeId: String?,
    val wikipediaUrl: String?,

    var isFavorite: Boolean = false
)

