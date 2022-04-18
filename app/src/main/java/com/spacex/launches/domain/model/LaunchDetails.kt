package com.spacex.launches.domain.model

import java.util.*

data class LaunchDetails(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val date: Date = Date(0),
    val imageUrl: String = "",
    val videoUrl: String = "",
    val youtubeId: String = "",
    val wikipediaUrl: String = "",
    val rocketName: String = "",
    val payloadMass: Number = 0,
    val isFavorite: Boolean = false
)
