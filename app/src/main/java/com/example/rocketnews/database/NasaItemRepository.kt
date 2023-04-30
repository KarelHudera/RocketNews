package com.example.rocketnews.database

import android.database.sqlite.SQLiteException
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
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

    suspend fun getAll(): List<NasaItem> {
        return try {
            withContext(Dispatchers.IO) {
                nasaItemDao.getAll()
            }
        } catch (ex: SQLiteException) {
            emptyList()
        }
    }
}
