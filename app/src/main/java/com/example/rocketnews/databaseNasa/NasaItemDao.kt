package com.example.rocketnews.databaseNasa

import androidx.room.*

@Dao
interface NasaItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(String: NasaItem)

    @Query("SELECT * FROM nasa_items")
    suspend fun getAll(): List<NasaItem>

    @Query("DELETE FROM nasa_items")
    suspend fun deleteAll()
}
