package com.spacex.launches.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "launches")
data class DbLaunch(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @ColumnInfo(name = "name")
    val name: String? = "",
    @ColumnInfo(name = "description")
    val description: String? = "",
    @ColumnInfo(name = "wikipedia")
    val wikipedia: String? = "",
    @ColumnInfo(name = "date")
    val date: Date = Date(0),
    @ColumnInfo(name = "image_small")
    val imageSmall: String? = "",
    @ColumnInfo(name = "image_large")
    val imageLarge: String? = "",
    @ColumnInfo(name = "video")
    val video: String? = "",
    @ColumnInfo(name = "youtube_id")
    val youtubeId: String? = "",
    @ColumnInfo(name = "rocket_id")
    val rocketId: String? = "",
    @ColumnInfo(name = "rocket_name")
    val rocketName: String? = "",
    @ColumnInfo(name = "payload_mass_kg")
    val payloadMassKg: Float? = 0f,
    @ColumnInfo(name = "payload_mass_lbs")
    val payloadMassLbs: Float? = 0f
)
