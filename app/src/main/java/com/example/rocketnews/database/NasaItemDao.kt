package com.example.rocketnews.database

import androidx.room.*

@Dao
interface NasaItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(nasaItem: NasaItem)

    @Query("SELECT * FROM nasa_items")
    suspend fun getAll(): List<NasaItem>

//    @Query("DELETE * FROM nasa_items ")
//    suspend fun delete(nasaItem: NasaItem)
}
