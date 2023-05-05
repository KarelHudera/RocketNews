package com.example.rocketnews.databaseSpaceX

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SpaceXItem::class], version = 1, exportSchema = false)
abstract class DatabaseSpaceX : RoomDatabase() {
    abstract fun spaceXItemDao(): SpaceXItemDao
}