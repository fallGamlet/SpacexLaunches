package com.spacex.launches.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LaunchDao {
    @Query("SELECT * FROM launches ORDER BY date DESC")
    suspend fun getAllLaunches(): List<DbLaunch>

    @Query("SELECT * FROM launches WHERE id = :id limit 1")
    suspend fun getLaunchById(id: String): DbLaunch

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg launches: DbLaunch)

    @Delete
    suspend fun delete(vararg launch: DbLaunch)

}
