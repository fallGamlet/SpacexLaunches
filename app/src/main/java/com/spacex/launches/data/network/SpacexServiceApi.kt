package com.spacex.launches.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


// https://api.spacexdata.com/v4/
interface SpacexServiceApi {

    @POST("launches/query")
    suspend fun getLaunches(@Body body: QueryArgsJson): Response<ResponseJson>

}
