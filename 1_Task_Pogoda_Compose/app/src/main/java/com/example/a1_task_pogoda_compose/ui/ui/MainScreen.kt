package com.example.a1_task_pogoda_compose

import WeatherViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.a1_task_pogoda_compose.ui.data.ForecastDay
import com.example.a1_task_pogoda_compose.ui.theme.Typography
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen() {
    val weatherViewModel: WeatherViewModel = viewModel()
    val weatherDataState by weatherViewModel.weatherData.collectAsState()
    val weatherData = weatherDataState
    val switcherState = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    val showError = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(true) }
    val temperatureType = remember { mutableStateOf("C") }
    val isRefreshing = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(weatherData) {
        isLoading.value = weatherData.isEmpty()
    }

    // Используем remember для сохранения backgroundColor
    val backgroundColor by weatherViewModel.backgroundColor.collectAsState()

    val location = weatherViewModel.location

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(backgroundColor)
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onVerticalDrag = { _, _ -> },
                    onDragEnd = {
                        if (!isRefreshing.value) {
                            isRefreshing.value = true
                            scope.launch {
                                weatherViewModel.updateLocation(weatherViewModel.location) { error ->
                                    showError.value = true
                                    errorMessage.value = error
                                }
                                isRefreshing.value = false
                            }
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            topBar = { MainTopBar(location, showDialog, switcherState, temperatureType) },
            containerColor = Color.Transparent
        ) { paddingValues ->
            if (isLoading.value) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Loading ...")
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(Color.Transparent)
                ) {
                    CurrentTemperature(
                        mainTypeTemperature(weatherData[0], temperatureType),
                        weatherData[0].day.condition.text,
                        modifier = Modifier
                            .weight(1f)
                            .background(Color.Transparent)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                            .background(Color.Transparent)
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(weatherData) { forecastDay ->
                                WeatherDayItem(forecastDay, temperatureType)
                            }
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = isRefreshing.value,
            enter = fadeIn(animationSpec = tween(500)),
            exit = fadeOut(animationSpec = tween(500))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x80000000))
                    .zIndex(1f),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        AnimatedVisibility(
            visible = showDialog.value,
            enter = fadeIn(animationSpec = tween(500)),
            exit = fadeOut(animationSpec = tween(500))
        ) {
            CityInputDialog(
                onDismiss = { showDialog.value = false },
                onSubmit = { newCity ->
                    weatherViewModel.updateLocation(newCity) { error ->
                        showError.value = true
                        errorMessage.value = error
                    }
                    showDialog.value = false
                }
            )
        }

        AnimatedVisibility(
            visible = showError.value,
            enter = fadeIn(animationSpec = tween(500)),
            exit = fadeOut(animationSpec = tween(500))
        ) {
            ErrorDialog(
                message = errorMessage.value,
                onDismiss = { showError.value = false }
            )
        }
    }
}




fun mainTypeTemperature(weatherDayData: ForecastDay, temperatureType: MutableState<String>): Double {
    return if (temperatureType.value == "C") {
        weatherDayData.day.avgtemp_c
    } else {
        weatherDayData.day.avgtemp_f
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun dataFormating(dataString: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dataString, formatter)
    val dayOfWeek = date.dayOfWeek
    return dayOfWeek.getDisplayName(TextStyle.FULL, Locale("en"))
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherDayItem(forecastDay: ForecastDay, temperatureType: MutableState<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val iconUrl = "https:${forecastDay.day.condition.icon}"
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = rememberImagePainter(iconUrl),
                contentDescription = "Weather Icon",
                modifier = Modifier.size(40.dp),
            )

            Text(
                text = forecastDay.day.condition.text,
                style = Typography.bodyLarge,
            )
        }
        Column() {
            Text(
                text = "${dataFormating(forecastDay.date)}/${mainTypeTemperature(forecastDay, temperatureType).toInt()}°",
                style = Typography.bodyLarge,
            )
        }
    }
}

@Composable
fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Error") },
        text = { Text(text = message) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    location: String,
    showDialog: MutableState<Boolean>,
    switcherState: MutableState<Boolean>,
    temperatureType: MutableState<String>
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            navigationIconContentColor = Color.Black,
            actionIconContentColor = Color.Black,
            titleContentColor = Color.Black
        ),
        title = {
            MainLocation(location)
        },
        navigationIcon = {
            IconButton(onClick = { showDialog.value = true }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
            }
        },
        actions = {
            Box {
                SwitchTemperatureMeasure(temperatureType, switcherState)
            }
        }
    )
}

@Composable
fun SwitchTemperatureMeasure(temperatureType: MutableState<String>, switcherState: MutableState<Boolean>) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(end = 16.dp)
    ) {
        Switch(
            checked = switcherState.value,
            onCheckedChange = {
                switcherState.value = it
                temperatureType.value = if (it) "F" else "C"
            }
        )
        Text(text = "C -> F")
    }
}

@Composable
fun MainLocation(location: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = location,
            style = Typography.bodyLarge,
            color = Color.Black
        )
    }
}

@Composable
fun CurrentTemperature(temperatureMeasurement: Double, weatherCondition: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainTemperature(temperatureMeasurement)
            MainCondition(weatherCondition)
        }
    }
}

@Composable
fun MainTemperature(temperatureMeasurement: Double) {
    Box {
        Text(
            text = buildAnnotatedString {
                append("${temperatureMeasurement.toInt()}")
                withStyle(style = SpanStyle(fontSize = Typography.titleLarge.fontSize * 0.5f, baselineShift = BaselineShift.Superscript)) {
                    append("°")
                }
            },
            style = Typography.titleLarge
        )
    }
}

@Composable
fun MainCondition(weatherCondition: String) {
    Box {
        Text(
            text = weatherCondition,
            style = Typography.bodyLarge
        )
    }
}

@Composable
fun CityInputDialog(
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit
) {
    val textState = remember { mutableStateOf(TextFieldValue()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Enter City") },
        text = {
            BasicTextField(
                value = textState.value,
                onValueChange = { textState.value = it },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(onClick = {
                onSubmit(textState.value.text)
            }) {
                Text("Submit")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}