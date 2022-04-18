package com.spacex.launches.data.repository

import com.spacex.launches.data.database.*
import com.spacex.launches.data.network.*
import com.spacex.launches.domain.model.LaunchDetails
import com.spacex.launches.domain.model.LaunchPreview
import com.spacex.launches.domain.repository.LaunchRepository
import java.util.*

class LaunchRepositoryImpl(
    private val launchDao: LaunchDao,
    private val favoriteDao: FavoriteDao,
    private val launchPreviewDao: LaunchPreviewDao,
    private val serviceApi: SpacexServiceApi,
): LaunchRepository {

    private val cacheLifetime: Long = 2*60*60*1000
    private var lastUpdates: Long = 0

    override suspend fun getLaunches(): List<LaunchPreview> {
        updateData()
        return getLocalLaunches()
    }

    private suspend fun updateData() {
        val now = System.currentTimeMillis()
        val lifetimeExpireAt = lastUpdates + cacheLifetime
        if (lifetimeExpireAt >= now) return

        val response = serviceApi.getLaunches(getQueryArgs())
        if (!response.isSuccessful) {
            throw Exception(response.message())
        }

        val dbLaunches = response.body()?.docs?.asSequence()
            ?.filter { !it.id.isNullOrBlank() }
            ?.map(::mapDbLaunch)
            ?.toList()
            ?.toTypedArray()

        if (!dbLaunches.isNullOrEmpty()) {
            launchDao.insertAll(*dbLaunches)
        }
        lastUpdates = now
    }

    private fun getQueryArgs(): QueryArgsJson {
        return QueryArgsJson(
            options = OptionsJson(
                select = listOf(
                    "id",
                    "name",
                    "details",
                    "links.patch",
                    "links.flickr",
                    "links.webcast",
                    "links.youtube_id",
                    "links.wikipedia",
                    "date_utc",
                    "date_unix",
                    "rocket",
                    "payloads"
                ),
                populate = listOf(
                    PopulateJson(
                        path = "payloads",
                        select = listOf("id", "name", "mass_kg", "mass_lbs")
                    ),
                    PopulateJson(
                        path = "rocket",
                        select = listOf("id", "name")
                    )
                ),
                pagination = false
            )
        )
/**
{
    "query": {},
    "options": {
        "select": [
            "id",
            "name",
            "details",
            "links.patch",
            "links.flickr",
            "links.webcast",
            "links.youtube_id",
            "links.wikipedia",
            "date_utc",
            "date_unix",
            "rocket",
            "payloads"
        ],
        "populate": [
            {
                "path": "payloads",
                "select": [ "id", "name", "mass_kg", "mass_lbs" ]
            },
            {
                "path": "rocket",
                "select": [ "id", "name" ]
            }
        ],
        "pagination": false
    }
}
 */
    }

    private fun mapDbLaunch(json: LaunchJson): DbLaunch {
        val imageLinks = getImageUrl(json.links)
        return DbLaunch(
            id = json.id!!,
            name = json.name ?: "",
            description = json.details ?: "",
            wikipedia = json.links?.wikipedia ?: "",
            date = Date(json.dateUnix?.times(1000) ?: 0L),
            imageSmall = imageLinks.first,
            imageLarge = imageLinks.second,
            video = json.links?.webcast ?: "",
            youtubeId = json.links?.youtubeId,
            rocketId = json.rocket?.id ?: "",
            rocketName = json.rocket?.name ?: "",
            payloadMassKg = getPayloadMassKg(json.payloads),
            payloadMassLbs = getPayloadMassLbs(json.payloads)
        )
    }

    private fun getImageUrl(links: LinksJson?): Pair<String, String> {
        if (links == null) return EMPTY_PAIR

        return links.patch
            ?.let { Pair(it.small?: "", it.large ?: "") }
            ?.takeIf { it.first.isNotBlank() || it.second.isNotBlank() }
            ?: getImageUrl(links.flickr)
    }

    private fun getImageUrl(flickr: FlickrJson?): Pair<String, String> {
        val hasNoData = flickr == null ||
                (flickr.small.isNullOrEmpty() && flickr.original.isNullOrEmpty())

        return if (hasNoData) EMPTY_PAIR
        else Pair(
            flickr?.small?.firstOrNull() ?: "",
            flickr?.original?.firstOrNull() ?: ""
        )
    }

    private fun getPayloadMassKg(payloads: List<PayloadJson>?): Float =
        payloads?.sumOf { it.massLbs?.toDouble() ?: 0.0 }
            ?.toFloat()
            ?: 0f

    private fun getPayloadMassLbs(payloads: List<PayloadJson>?): Float =
        payloads?.sumOf { it.massLbs?.toDouble() ?: 0.0 }
            ?.toFloat()
            ?: 0f

    private suspend fun getLocalLaunches(): List<LaunchPreview> {
        return launchPreviewDao.getAllLaunchPreviews()
            .map(::mapLaunchPreview)
    }

    private fun mapLaunchPreview(data: DbLaunchPreview): LaunchPreview {
        return LaunchPreview(
            id = data.id,
            name = data.name ?: "",
            date = data.date,
            imageUrl = data.imageSmall ?: "",
            isFavorite = data.isFavorite
        )
    }

    override suspend fun getLaunchDetails(id: String): LaunchDetails {
        val data = launchDao.getLaunchById(id)
        val isFavorite = favoriteDao.isFavorite(id)
        return LaunchDetails(
            id = data.id,
            name = data.name ?: "",
            description = data.description ?: "",
            imageUrl = data.imageLarge ?: "",
            videoUrl = data.video ?: "",
            youtubeId = data.youtubeId ?: "",
            wikipediaUrl = data.wikipedia ?: "",
            rocketName = data.rocketName ?: "",
            payloadMass = data.payloadMassKg ?: 0,
            isFavorite = isFavorite
        )
    }

    override suspend fun setFavorite(id: String, favorite: Boolean) {
        val data = DbFavorite(id)
        if (favorite) {
            favoriteDao.addToFavourites(data)
        } else {
            favoriteDao.removeFromFavourites(data)
        }
    }

    companion object {
        private val EMPTY_PAIR = Pair("", "")
    }
}
