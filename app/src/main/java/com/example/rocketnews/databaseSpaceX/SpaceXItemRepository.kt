package com.example.rocketnews.databaseSpaceX

import android.database.sqlite.SQLiteException
import androidx.room.Room
import com.example.rocketnews.App
import com.example.rocketnews.databaseNasa.DatabaseNasa
import com.example.rocketnews.databaseNasa.NasaItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SpaceXItemRepository {
    private val database: DatabaseSpaceX = Room.databaseBuilder(
        App.instance.applicationContext,
        DatabaseSpaceX::class.java,
        "database_spacex"
    ).build()

    private val spaceXItemDao = database.spaceXItemDao()

    suspend fun getAllSpaceX(): List<SpaceXItem> {
        return try {
            withContext(Dispatchers.IO) {
                spaceXItemDao.getAllSpaceX()
            }
        } catch (ex: SQLiteException) {
            emptyList()
        }
    }
    suspend fun insertSpaceX(spaceXItem: SpaceXItem) {
        withContext(Dispatchers.IO) {
            spaceXItemDao.insertSpaceX(spaceXItem)
        }
    }

    suspend fun deleteSpaceX(spaceXItem: SpaceXItem) {
        withContext(Dispatchers.IO) {
            spaceXItemDao.deleteSpaceX(spaceXItem)
        }
    }
}
