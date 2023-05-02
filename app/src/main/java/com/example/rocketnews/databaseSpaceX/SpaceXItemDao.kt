package com.example.rocketnews.databaseSpaceX

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SpaceXItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpaceX(spaceXItem: SpaceXItem)

    @Query("SELECT * FROM spacex_items")
    suspend fun getAllSpaceX(): List<SpaceXItem>

//    @Query("DELETE FROM spacex_items WHERE id = :itemId")
//    suspend fun deleteSpaceX(itemId: Int)

    @Delete
    suspend fun deleteSpaceX(spaceXItem: SpaceXItem)
}