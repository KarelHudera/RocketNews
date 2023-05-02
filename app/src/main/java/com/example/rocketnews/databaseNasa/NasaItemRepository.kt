package com.example.rocketnews.databaseNasa

import android.database.sqlite.SQLiteException
import androidx.room.Room
import com.example.rocketnews.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NasaItemRepository {
    private val database: DatabaseNasa = Room.databaseBuilder(
        App.instance.applicationContext,
        DatabaseNasa::class.java,
        "database_nasa"
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