package com.example.a1_task_pogoda_compose.ui.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainScreen(weatherViewModel: WeatherViewModel = viewModel()){
    val weatherState by weatherViewModel.weatherstate.collectAsState()
    val backgroundColor by animateColorAsState(
        targetValue = when(weatherState.WeatherState) {
            WeatherStates.SUNNY -> Color.Yellow
            WeatherStates.RAINY-> Color.Blue
            WeatherStates.CLOUDLY -> Color.Gray
            WeatherStates.SNOWY-> Color.White
        },
        animationSpec = tween(durationMillis = 1000)
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        WeatherBox(weatherState)
        ButtonBox(weatherViewModel)
    }
}

@Composable
fun WeatherBox(weatherFeatures: WeatherFeatures){

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(1000))
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)

            ) {

                Text(
                    text = "${weatherFeatures.temperature}"
                )
                Image(
                    painter = painterResource(id = weatherFeatures.weathericon),
                    contentDescription = null
                )
            }
            Text(
                text = weatherFeatures.state
            )
        }
    }
}


@Composable
fun ButtonBox(weatherViewModel: WeatherViewModel){
    Button(onClick = {
        weatherViewModel.ChangeWeatherFeatures()
    }) {
        Text(text = "Обновить данные")
    }
}


