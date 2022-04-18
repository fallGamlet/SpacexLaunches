package com.spacex.launches

import android.content.Context
import androidx.room.Room
import com.spacex.launches.data.database.AppDatabase
import com.spacex.launches.data.network.SpacexApiFactory
import com.spacex.launches.data.network.SpacexServiceApi
import com.spacex.launches.data.repository.LaunchRepositoryImpl
import com.spacex.launches.domain.repository.LaunchRepository

class AppDataFacade(
    context: Context
): RepositoryProvider {

    private val db: AppDatabase
    private val serviceApi: SpacexServiceApi

    init {
        db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "app_data.db"
        ).build()

        serviceApi = SpacexApiFactory()
            .createApi(BuildConfig.DEBUG)
    }

    override fun getLaunchRepository(): LaunchRepository {
        return LaunchRepositoryImpl(
            launchDao = db.getLaunchDao(),
            favoriteDao = db.getFavoriteDao(),
            launchPreviewDao = db.getLaunchPreviewDao(),
            serviceApi = serviceApi
        )
    }
}
