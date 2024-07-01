package com.example.a1_task_pogoda_compose.network

import com.example.a1_task_pogoda_compose.ui.data.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("current.json")
    suspend fun getcurrentWeather(
        @Query("key") apikey: String,
        @Query("q") location: String,
    ): WeatherResponse

    @GET("forecast.json")
    suspend fun getforecastWheather(
        @Query("key") apikey: String,
        @Query("q") location: String,
        @Query("days") neededdays: Int,
    ): WeatherResponse
}
