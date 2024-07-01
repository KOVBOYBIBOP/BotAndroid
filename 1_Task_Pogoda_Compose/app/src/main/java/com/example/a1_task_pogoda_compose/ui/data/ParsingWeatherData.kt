package com.example.a1_task_pogoda_compose.ui.data

data class WeatherResponse(
    val forecast: Forecast,
    val location: Location
)

data class Location(
    val name:String

)
data class Forecast(
    val forecastday: List<ForecastDay>
)

data class ForecastDay(
    val date: String,
    val day: Day
)

data class Day(
    val avgtemp_c: Double,
    val avgtemp_f: Double,
    val condition: Condition
)

data class Condition(
    val text: String,
    val icon: String
)
