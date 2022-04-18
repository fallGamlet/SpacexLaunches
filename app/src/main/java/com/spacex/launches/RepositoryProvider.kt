package com.spacex.launches

import com.spacex.launches.domain.repository.LaunchRepository

interface RepositoryProvider {
    fun getLaunchRepository(): LaunchRepository
}
