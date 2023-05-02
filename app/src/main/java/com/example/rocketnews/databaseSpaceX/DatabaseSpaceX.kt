package com.example.rocketnews.databaseSpaceX

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rocketnews.databaseNasa.NasaItem
import com.example.rocketnews.databaseNasa.NasaItemDao

@Database(entities = [SpaceXItem::class], version = 1, exportSchema = false)
abstract class DatabaseSpaceX : RoomDatabase() {
    abstract fun spaceXItemDao(): SpaceXItemDao
}