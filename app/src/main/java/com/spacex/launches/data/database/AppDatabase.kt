package com.spacex.launches.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.*

@Database(
    entities = [
        DbLaunch::class,
        DbFavorite::class,
   ],
    views = [
        DbLaunchPreview::class
    ],
    version = 1
)
@TypeConverters(AppDatabase.Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getLaunchDao(): LaunchDao

    abstract fun getFavoriteDao(): FavoriteDao

    abstract fun getLaunchPreviewDao(): LaunchPreviewDao

    object Converters {
        @TypeConverter
        fun fromTimestamp(value: Long?): Date {
            return Date(value ?: 0L)
        }

        @TypeConverter
        fun dateToTimestamp(date: Date?): Long {
            return date?.time ?: 0L
        }
    }
}
