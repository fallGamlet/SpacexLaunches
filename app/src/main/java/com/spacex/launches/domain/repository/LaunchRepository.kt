package com.spacex.launches.domain.repository

import com.spacex.launches.domain.model.LaunchDetails
import com.spacex.launches.domain.model.LaunchPreview

interface LaunchRepository {
    /**
     * Get all preview of launches
     */
    suspend fun getLaunches(): List<LaunchPreview>

    /**
     * Get launch details info
     * @param id String is ID of launch
     */
    suspend fun getLaunchDetails(id: String): LaunchDetails

    /**
     * Set favorite marker for the launch
     */
    suspend fun setFavorite(id: String, favorite: Boolean)
}
