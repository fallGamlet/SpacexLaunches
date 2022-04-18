package com.spacex.launches.ui.launches.details

import java.util.*


sealed class LaunchDetailsViewState(
    val data: DetailsItem
) {

    class Loading(
        data: DetailsItem = EMPTY_DATA
    ): LaunchDetailsViewState(data)

    class Success(
        data: DetailsItem = EMPTY_DATA
    ): LaunchDetailsViewState(data)

    class Error(
        val exception: Throwable,
        data: DetailsItem = EMPTY_DATA
    ): LaunchDetailsViewState(data)

    data class DetailsItem(
        val id: String = "",
        val title: String = "",
        val description: String = "",
        val date: Date = Date(0),
        val rocketName: String = "",
        val payloadMass: Number = 0,
        val imageUrl: String = "",
        val videoUrl: String = "",
        val youtubeId: String = "",
        val wikipediaUrl: String = "",
        val isFavorite: Boolean = false
    )

    companion object {
        val EMPTY_DATA = DetailsItem()
    }
}
