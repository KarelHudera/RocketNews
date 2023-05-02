package com.example.rocketnews.databaseNasa

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nasa_items")
data class NasaItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String?,
    val explanation: String?,
    val hdUrl: String?,
    val title: String?,
)
