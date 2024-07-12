import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a1_task_pogoda_compose.repository.retrofitRequests
import com.example.a1_task_pogoda_compose.ui.data.ForecastDay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.ui.graphics.Color
import com.example.a1_task_pogoda_compose.R

class WeatherViewModel() : ViewModel() {

    private var _weatherData = MutableStateFlow<List<ForecastDay>>(emptyList())
    val weatherData: StateFlow<List<ForecastDay>> = _weatherData
    private var currentLocation = "Moscow"
    var location = currentLocation


    private val _backgroundColor = MutableStateFlow(R.drawable.ic_snow)
    val backgroundColor: StateFlow<Int> = _backgroundColor

    fun updateLocation(newLocation: String, onError: (String) -> Unit) {
           _weatherData.value = emptyList()
            fetchWeatherData(newLocation, onError)
            location = newLocation

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
                    updateBackgroundColor(result[0].day.condition.text) // Обновляем цвет фона
                } else {
                    onError("No weather data found for the specified location.")
                }
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error fetching data", e)
                onError("Error fetching data: ${e.message}")
            }
        }
    }

    private fun updateBackgroundColor(description: String) {
        _backgroundColor.value = getBackgroundColor(description)
    }

    private fun getBackgroundColor(description: String): Int {
        Log.d("BackCheck","ejnfw;")
        return when {
            "rain" in description.lowercase() -> R.drawable.ic_rain
            "cloud" in description.lowercase() -> R.drawable.ic_cloud
            "sun" in description.lowercase() -> R.drawable.ic_sun
            "snow" in description.lowercase() -> R.drawable.ic_snow
            else -> R.drawable.ic_base
        }
    }
}
