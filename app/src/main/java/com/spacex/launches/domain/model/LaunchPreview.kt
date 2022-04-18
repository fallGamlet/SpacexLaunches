package com.spacex.launches.domain.model

import java.util.*

data class LaunchPreview(
    val id: String = "",
    val name: String = "",
    val date: Date = Date(0),
    val imageUrl: String = "",
    val isFavorite: Boolean = false
)
