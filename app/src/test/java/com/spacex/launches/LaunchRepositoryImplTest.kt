package com.spacex.launches

import com.spacex.launches.data.database.*
import com.spacex.launches.data.network.SpacexApiFactory
import com.spacex.launches.data.repository.LaunchRepositoryImpl
import com.spacex.launches.domain.repository.LaunchRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class LaunchRepositoryImplTest {

    private lateinit var launchRepository: LaunchRepository

    @Before
    fun prepare() {
        println("-------- prepare started... ----------")
        val dao = DummyLaunchDao()
        launchRepository = LaunchRepositoryImpl(
            launchDao = dao,
            launchPreviewDao = dao,
            favoriteDao = dao,
            serviceApi = SpacexApiFactory()
                .createApi(BuildConfig.DEBUG)
        )
        println("-------- prepare finished ------------")
    }

    @Test
    fun testLoadData() {
        runBlocking {
            val launches = launchRepository.getLaunches()
            assert(launches.isNotEmpty()) { "Has no launches" }
            println("--- Launches --- start ---")
            launches.forEach {
                println(it)
            }
            println("--- Launches --- end -----")


            println("--- Launch details --- start ---")
            val firstItem = launches.first()
            val launchDetails = launchRepository.getLaunchDetails(firstItem.id)
            println(firstItem)
            println(launchDetails)
            assert(firstItem.id == launchDetails.id) { "Wrong launch details got" }
            println("--- Launch details --- end -----")
        }
    }


    private class DummyLaunchDao:
        LaunchDao,
        LaunchPreviewDao,
        FavoriteDao
    {
        private var data: List<DbLaunch> = emptyList()

        override suspend fun getAllLaunches(): List<DbLaunch> {
            return data
        }

        override suspend fun getLaunchById(id: String): DbLaunch {
            return data.first { it.id == id }
        }

        override suspend fun insertAll(vararg launches: DbLaunch) {
            data = launches.toList()
        }

        override suspend fun delete(vararg launch: DbLaunch) {
            TODO("Not yet implemented")
        }

        override suspend fun getAllLaunchPreviews(): List<DbLaunchPreview> {
            return data.map(::mapLaunchPreview)
        }

        private fun mapLaunchPreview(data: DbLaunch) =
            DbLaunchPreview(
                id = data.id,
                name = data.name,
                date = data.date,
                imageSmall = data.imageSmall,
                imageLarge = data.imageLarge,
                isFavorite = false
            )

        override suspend fun getLaunchPreviewById(id: String): DbLaunchPreview {
            return getLaunchById(id).let(::mapLaunchPreview)
        }

        override suspend fun addToFavourites(vararg favorite: DbFavorite) {
            TODO("Not yet implemented")
        }

        override suspend fun removeFromFavourites(vararg record: DbFavorite) {
            TODO("Not yet implemented")
        }

        override suspend fun isFavorite(id: String): Boolean = false

    }
}
