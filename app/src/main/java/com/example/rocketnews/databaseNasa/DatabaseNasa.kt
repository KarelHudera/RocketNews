package com.example.rocketnews.databaseNasa

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NasaItem::class], version = 1, exportSchema = false)
abstract class DatabaseNasa : RoomDatabase() {
    abstract fun nasaItemDao(): NasaItemDao
}