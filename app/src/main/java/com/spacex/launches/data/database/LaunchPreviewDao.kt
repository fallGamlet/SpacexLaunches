package com.spacex.launches.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LaunchPreviewDao {
    @Query("SELECT * FROM launch_previews ORDER BY date DESC")
    suspend fun getAllLaunchPreviews(): List<DbLaunchPreview>

    @Query("SELECT * FROM launch_previews WHERE id = :id limit 1")
    suspend fun getLaunchPreviewById(id: String): DbLaunchPreview

}
