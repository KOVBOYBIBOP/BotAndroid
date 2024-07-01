package com.example.a1_task_pogoda_compose.ui.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a1_task_pogoda_compose.repository.retrofitRequests
import com.example.a1_task_pogoda_compose.ui.data.ForecastDay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log

class WeatherViewModel() : ViewModel() {

    private val _weatherData = MutableStateFlow<List<ForecastDay>>(emptyList())
    val weatherData: StateFlow<List<ForecastDay>> = _weatherData
    private var currentLocation = "Moscow"
    var location = currentLocation
        private set

    fun updateLocation(newLocation: String, onError: (String) -> Unit) {
        if (currentLocation != newLocation) {
            fetchWeatherData(newLocation, onError)
        }
    }

    init {
        fetchWeatherData(currentLocation){}
    }

    private fun fetchWeatherData(location: String, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val result = retrofitRequests(
                    "0be8b228b80947eda81145325242606",
                    location,
                    "https://api.weatherapi.com/v1/"
                )
                if (result.isNotEmpty()) {
                    Log.d("WeatherViewModel", "Data fetched successfully: $result")
                    _weatherData.value = result
                    currentLocation = location
                    this@WeatherViewModel.location = location
                } else {
                    onError("No weather data found for the specified location.")
                }
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error fetching data", e)
                onError("Error fetching data: ${e.message}")
            }
        }
    }
}
