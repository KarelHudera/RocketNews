package com.example.rocketnews.databaseSpaceX

import androidx.room.*

@Dao
interface SpaceXItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpaceX(spaceXItem: SpaceXItem)

    @Query("SELECT * FROM spacex_items")
    suspend fun getAllSpaceX(): List<SpaceXItem>

    @Delete
    suspend fun deleteSpaceX(spaceXItem: SpaceXItem)

    @Query("DELETE FROM spacex_items")
    suspend fun deleteAllSpaceX()
}