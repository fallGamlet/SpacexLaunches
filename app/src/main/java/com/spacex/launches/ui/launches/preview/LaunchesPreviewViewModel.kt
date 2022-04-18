package com.spacex.launches.ui.launches.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.spacex.launches.domain.model.LaunchPreview
import com.spacex.launches.domain.repository.LaunchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch

class LaunchesPreviewViewModel(
    private val launchRepository: LaunchRepository
) : ViewModel() {

    private var _viewState =
        MutableStateFlow<LaunchPreviewsViewState>(LaunchPreviewsViewState.Idle())
    val viewState: StateFlow<LaunchPreviewsViewState> = _viewState

    fun loadData() {
        viewModelScope.launch {
            _viewState.value = LaunchPreviewsViewState.Loading(_viewState.value.items)

            try {
                val launches = launchRepository.getLaunches()
                    .map(::mapLaunchPreview)
                _viewState.value = LaunchPreviewsViewState.Success(launches)
            } catch (e: Throwable) {
                _viewState.value = LaunchPreviewsViewState.Error(
                    exception = e,
                    items = _viewState.value.items
                )
            }
        }
    }

    private fun mapLaunchPreview(data: LaunchPreview) =
        LaunchPreviewsViewState.PreviewItem(
            id = data.id,
            title = data.name,
            date = data.date,
            imageUrl = data.imageUrl,
            isFavorite = data.isFavorite
        )

    fun toggleFavorite(launchId: String) {
        viewModelScope.launch {
            val launches = _viewState.value.items
            val index = launches.indexOfFirst { it.id == launchId }
            if (index == -1) return@launch

            val launch = launches[index]
                .let { it.copy(isFavorite = !it.isFavorite) }
            launchRepository.setFavorite(launchId, launch.isFavorite)
            val updatedLaunches = launches.toMutableList()
                .also {
                    it[index] = launch
                }.toList()

            _viewState.value = LaunchPreviewsViewState.Success(updatedLaunches)
        }
    }



    class Factory(private val repository: LaunchRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LaunchesPreviewViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LaunchesPreviewViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}
