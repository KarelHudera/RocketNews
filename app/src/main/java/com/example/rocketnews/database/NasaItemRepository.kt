package com.example.rocketnews.database

import android.database.sqlite.SQLiteException
import androidx.room.Room
import com.example.rocketnews.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NasaItemRepository {
    private val database: AppDatabase = Room.databaseBuilder(
        App.instance.applicationContext,
        AppDatabase::class.java,
        "app_database"
    ).build()

    private val nasaItemDao = database.nasaItemDao()

    suspend fun insert(nasaItem: NasaItem) {
        try {
            nasaItemDao.insert(nasaItem)
        } catch (ex: SQLiteException) {
            // Handle SQLiteExceptions here
        }
    }

    suspend fun getAll(): List<NasaItem> {
        return try {
            nasaItemDao.getAll()
        } catch (ex: SQLiteException) {
            emptyList()
        }
    }

//    suspend fun delete(nasaItem: NasaItem) {
//        withContext(Dispatchers.IO) {
//            nasaItemDao.delete=(nasaItem)
//        }
//    }
}