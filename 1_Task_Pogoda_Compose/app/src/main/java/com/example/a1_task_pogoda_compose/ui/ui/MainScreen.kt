//package com.example.a1_task_pogoda_compose.ui.ui
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.animateColorAsState
//import androidx.compose.animation.core.tween
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//
//
//@Composable
//fun WeatherApp(weatherViewModel: WeatherViewModel = viewModel()) {
//    val weatherState by weatherViewModel.weatherState.collectAsState()
//
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
//        modifier = Modifier.fillMaxSize()
//    ) {
//        WeatherCard(weatherState)
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(onClick = { weatherViewModel.updateWeather() }) {
//            Text("Update Weather")
//        }
//    }
//}
//
//@Composable
//fun WeatherCard(weatherState: WeatherState) {
//    val backgroundColor by animateColorAsState(
//        targetValue = when (weatherState.condition) {
//            WeatherCondition.SUNNY -> androidx.compose.ui.graphics.Color.Yellow
//            WeatherCondition.RAINY -> androidx.compose.ui.graphics.Color.Blue
//            WeatherCondition.CLOUDY -> androidx.compose.ui.graphics.Color.Gray
//            WeatherCondition.SNOWY -> androidx.compose.ui.graphics.Color.White
//        },
//        animationSpec = tween(durationMillis = 1000)
//    )
//
//    AnimatedVisibility(
//        visible = true,
//        enter = fadeIn(animationSpec = tween(1000)),
//        exit = fadeOut(animationSpec = tween(1000))
//    ) {
//        Card(
//            shape = RoundedCornerShape(16.dp),
//            modifier = Modifier
//                .padding(16.dp)
//                .background(backgroundColor)
//                .fillMaxWidth()
//        ) {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center,
//                modifier = Modifier.padding(16.dp)
//            ) {
//                Image(
//                    painter = painterResource(id = weatherState.weatherIcon),
//                    contentDescription = null,
//                    modifier = Modifier.size(64.dp)
//                )
//                Text(
//                    text = "${weatherState.temperature}°C",
//                    style = MaterialTheme.typography.bodyLarge,
//                    textAlign = TextAlign.Center
//                )
//                Text(
//                    text = weatherState.description,
//                    style = MaterialTheme.typography.labelSmall,
//                    textAlign = TextAlign.Center
//                )
//            }
//        }
//    }
//}
//
//enum class WeatherCondition {
//    SUNNY, RAINY, CLOUDY, SNOWY
//}
//
//data class WeatherState(
//    val temperature: Int,
//    val weatherIcon: Int,
//    val description: String,
//    val condition: WeatherCondition
//)
//
////class WeatherViewModel : ViewModel() {
////    private val _weatherState = MutableStateFlow(
////        WeatherState(25, R.drawable.ic_sun, "Sunny", WeatherCondition.SUNNY)
////    )
////    val weatherState: StateFlow<WeatherState> = _weatherState
////
////    fun updateWeather() {
////        // Here you can implement logic to get new weather data.
////        // For demonstration, we will just update with some random values.
////        val newCondition = WeatherCondition.entries.random()
////        val newIcon = when (newCondition) {
////            WeatherCondition.SUNNY -> R.drawable.ic_sun
////            WeatherCondition.RAINY -> R.drawable.ic_rain
////            WeatherCondition.CLOUDY -> R.drawable.ic_cloud
////            WeatherCondition.SNOWY -> R.drawable.ic_snow
////        }
////        val newDescription = when (newCondition) {
////            WeatherCondition.SUNNY -> "Sunny"
////            WeatherCondition.RAINY -> "Rainy"
////            WeatherCondition.CLOUDY -> "Cloudy"
////            WeatherCondition.SNOWY -> "Snowy"
////        }
////        _weatherState.value = WeatherState(
////            temperature = (0..40).random(),
////            weatherIcon = newIcon,
////            description = newDescription,
////            condition = newCondition
////        )
////    }
////}