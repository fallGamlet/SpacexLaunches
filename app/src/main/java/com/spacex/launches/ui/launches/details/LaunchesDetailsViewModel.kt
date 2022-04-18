package com.spacex.launches.ui.launches.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.spacex.launches.domain.model.LaunchDetails
import com.spacex.launches.domain.repository.LaunchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LaunchesDetailsViewModel(
    private val launchRepository: LaunchRepository
) : ViewModel() {

    private var _viewState =
        MutableStateFlow<LaunchDetailsViewState>(LaunchDetailsViewState.Loading())
    val viewState: StateFlow<LaunchDetailsViewState> = _viewState


    fun setData(launchId: String) {
        viewModelScope.launch {
            val data = _viewState.value.data
                .takeIf { it.id == launchId }
                ?: LaunchDetailsViewState.EMPTY_DATA

            _viewState.value = LaunchDetailsViewState.Loading(data)

            try {
                val launch = launchRepository.getLaunchDetails(launchId)
                    .let(::mapLaunchDetails)
                _viewState.value = LaunchDetailsViewState.Success(launch)
            } catch (e: Throwable) {
                _viewState.value = LaunchDetailsViewState.Error(
                    exception = e,
                    data = data
                )
            }
        }
    }

    private fun mapLaunchDetails(data: LaunchDetails) =
        LaunchDetailsViewState.DetailsItem(
            id = data.id,
            title = data.name,
            description = data.description,
            date = data.date,
            rocketName = data.rocketName,
            payloadMass = data.payloadMass,
            imageUrl = data.imageUrl,
            videoUrl = data.videoUrl,
            youtubeId = data.youtubeId,
            wikipediaUrl = data.wikipediaUrl,
            isFavorite = data.isFavorite,
        )

    fun toggleFavorite() {
        viewModelScope.launch {
            val data = _viewState.value.data
                .let { it.copy(isFavorite = !it.isFavorite) }

            launchRepository.setFavorite(data.id, data.isFavorite)

            _viewState.value = LaunchDetailsViewState.Success(data)
        }
    }



    class Factory(private val repository: LaunchRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LaunchesDetailsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LaunchesDetailsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}
