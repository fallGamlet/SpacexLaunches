package com.spacex.launches.data.network

import com.google.gson.annotations.SerializedName

data class QueryArgsJson (
    val query: QueryJson = QueryJson(),
    val options: OptionsJson = OptionsJson()
)

data class OptionsJson (
    val select: List<String>? = null,
    val populate: List<PopulateJson>? = null,
    val pagination: Boolean = false
)

data class PopulateJson (
    val path: String,
    val select: List<String>? = null
)

data class QueryJson(
    @SerializedName("date_unix")
    val dateUnix: DateUnixIntervalJson? = null,
    @SerializedName("date_utc")
    val dateUtc: DateUnixIntervalJson? = null
)

data class DateUnixIntervalJson(
    @SerializedName("\$gte")
    val gte: Long? = null,
    @SerializedName("\$lte")
    val lte: Long? = null
)

data class DateUtcIntervalJson(
    @SerializedName("\$gte")
    val gte: String? = null,
    @SerializedName("\$lte")
    val lte: String? = null
)
