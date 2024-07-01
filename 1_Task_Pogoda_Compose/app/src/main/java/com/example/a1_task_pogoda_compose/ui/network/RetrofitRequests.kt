package com.example.a1_task_pogoda_compose.repository

import com.example.a1_task_pogoda_compose.network.WeatherApi
import com.example.a1_task_pogoda_compose.ui.data.ForecastDay
import com.example.a1_task_pogoda_compose.ui.data.WeatherResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Log

suspend fun retrofitRequests(apiKey: String, location: String, baseUrl: String): List<ForecastDay> {
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val weatherApi = retrofit.create(WeatherApi::class.java)
    val daysCount = 8

    return try {
        val response: WeatherResponse = weatherApi.getforecastWheather(apiKey, location, daysCount)
        Log.d("retrofitRequests", "API response: ${response.forecast.forecastday}")
        response.forecast.forecastday
    } catch (e: Exception) {
        Log.e("retrofitRequests", "Error fetching data", e)
        emptyList()
    }
}
