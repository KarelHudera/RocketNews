package com.example.rocketnews.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NasaItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun nasaItemDao(): NasaItemDao
}