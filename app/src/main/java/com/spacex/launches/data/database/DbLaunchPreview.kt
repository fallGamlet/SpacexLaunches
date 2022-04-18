package com.spacex.launches.data.database

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@DatabaseView(
    value = """
        SELECT id, name, date, image_small, image_large, 
            id in (SELECT id from favourites) as is_favorite
        FROM launches
    """,
    viewName = "launch_previews"
)
data class DbLaunchPreview(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @ColumnInfo(name = "name")
    val name: String? = "",
    @ColumnInfo(name = "date")
    val date: Date = Date(0),
    @ColumnInfo(name = "image_small")
    val imageSmall: String? = "",
    @ColumnInfo(name = "image_large")
    val imageLarge: String? = "",
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false
)
