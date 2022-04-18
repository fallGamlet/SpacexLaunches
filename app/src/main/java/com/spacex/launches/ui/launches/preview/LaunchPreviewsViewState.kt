package com.spacex.launches.ui.launches.preview

import java.util.*


sealed class LaunchPreviewsViewState(
    val items: List<PreviewItem> = emptyList()
) {

    class Idle : LaunchPreviewsViewState()

    class Loading(
        items: List<PreviewItem> = emptyList()
    ): LaunchPreviewsViewState(items)

    class Success(
        items: List<PreviewItem> = emptyList()
    ): LaunchPreviewsViewState(items)

    class Error(
        val exception: Throwable,
        items: List<PreviewItem> = emptyList()
    ): LaunchPreviewsViewState(items)

    data class PreviewItem(
        val id: String = "",
        val title: String = "",
        val date: Date = Date(0),
        val imageUrl: String = "",
        val isFavorite: Boolean = false
    )
}
