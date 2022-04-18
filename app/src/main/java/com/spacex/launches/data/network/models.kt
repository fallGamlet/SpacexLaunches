package com.spacex.launches.data.network

import com.google.gson.annotations.SerializedName

data class ResponseJson (
    val docs: List<LaunchJson>? = null,
    val totalDocs: Long? = null,
    val offset: Long? = null,
    val limit: Long? = null,
    val totalPages: Long? = null,
    val page: Long? = null,
    val pagingCounter: Long? = null,
    val hasPrevPage: Boolean? = null,
    val hasNextPage: Boolean? = null,
    val prevPage: Long? = null,
    val nextPage: Long? = null
)

data class LaunchJson (
    val id: String? = null,
    val links: LinksJson? = null,
    val rocket: RocketJson? = null,
    val details: String? = null,
    val payloads: List<PayloadJson>? = null,
    val name: String? = null,
    @SerializedName("date_utc")
    val dateUTC: String? = null,
    @SerializedName("date_unix")
    val dateUnix: Long? = null
)

data class LinksJson (
    val patch: PatchJson? = null,
    val flickr: FlickrJson? = null,
    val webcast: String? = null,
    @SerializedName("youtube_id")
    val youtubeId: String? = null,
    val wikipedia: String? = null
)

data class FlickrJson (
    val small: List<String>? = null,
    val original: List<String>? = null
)

data class PatchJson (
    val small: String? = null,
    val large: String? = null
)

data class PayloadJson (
    val id: String? = null,
    val name: String? = null,
    @SerializedName("mass_kg")
    val massKg: Float? = null,
    @SerializedName("mass_lbs")
    val massLbs: Float? = null,
)

data class RocketJson (
    val id: String? = null,
    val name: String? = null
)
