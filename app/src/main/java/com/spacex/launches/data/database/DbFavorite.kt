package com.spacex.launches.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class DbFavorite(
    @PrimaryKey(autoGenerate = false)
    val id: String
)
