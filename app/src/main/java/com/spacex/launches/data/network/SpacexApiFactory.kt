package com.spacex.launches.data.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class SpacexApiFactory() {

    fun createApi(logsEnabled: Boolean): SpacexServiceApi {
        val clientBuilder = OkHttpClient.Builder()
            .callTimeout(30, TimeUnit.SECONDS)

        if (logsEnabled) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(interceptor)
//            clientBuilder.addNetworkInterceptor(interceptor)
        }

        return Retrofit.Builder()
            .baseUrl("https://api.spacexdata.com/v4/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientBuilder.build())
            .build()
            .create(SpacexServiceApi::class.java)
    }

}
