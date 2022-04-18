package com.spacex.launches.data.database

import androidx.room.*

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToFavourites(vararg favorite: DbFavorite)

    @Delete
    suspend fun removeFromFavourites(vararg record: DbFavorite)

    @Query("SELECT count(id) > 0 FROM favourites WHERE id == :id")
    suspend fun isFavorite(id: String): Boolean

}
