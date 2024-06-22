package com.example.a1_task_pogoda_compose.ui.ui

import androidx.lifecycle.ViewModel
import com.example.a1_task_pogoda_compose.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

enum class WeatherStates {
    SUNNY,
    RAINY,
    SNOWY,
    CLOUDLY,
}

data class WeatherFeatures(
    val temperature: Int,
    val state: String,
    val weathericon: Int,
    val WeatherState: WeatherStates,
)


fun сhangeWeatherState():Pair<Int, WeatherStates>{
    val randomTemperature = Random.nextInt(0,100)
    val randomWeatherStates = WeatherStates.entries.random()
    return Pair(randomTemperature, randomWeatherStates)
}

class WeatherViewModel:ViewModel() {
    private val WeatherState = MutableStateFlow(
        WeatherFeatures(25, "Sunny", R.drawable.ic_sun, WeatherStates.SUNNY)
    )

    val weatherstate:StateFlow<WeatherFeatures> = WeatherState
    fun ChangeWeatherFeatures(){
        val (newtemperature, newweatherstate) = сhangeWeatherState()
        val newicon = when(newweatherstate){
            WeatherStates.SUNNY->R.drawable.ic_sun
            WeatherStates.CLOUDLY->R.drawable.ic_cloud
            WeatherStates.RAINY->R.drawable.ic_rain
            WeatherStates.SNOWY->R.drawable.ic_snow
        }
        val newstate =when(newweatherstate){
            WeatherStates.SUNNY->"Sunny"
            WeatherStates.CLOUDLY->"Cloudly"
            WeatherStates.RAINY->"Rainy"
            WeatherStates.SNOWY->"Snowy"
        }
        WeatherState.value = WeatherFeatures(
            newtemperature,
            newstate,
            newicon,
            newweatherstate,
        )
    }

}


