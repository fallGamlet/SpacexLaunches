package com.spacex.launches

import android.app.Application
import com.spacex.launches.domain.repository.LaunchRepository

class App: Application(), RepositoryProvider {

    private lateinit var appDataFacade: AppDataFacade

    override fun onCreate() {
        super.onCreate()
        appDataFacade = AppDataFacade(this)
    }

    override fun getLaunchRepository(): LaunchRepository {
        return appDataFacade.getLaunchRepository()
    }

}
