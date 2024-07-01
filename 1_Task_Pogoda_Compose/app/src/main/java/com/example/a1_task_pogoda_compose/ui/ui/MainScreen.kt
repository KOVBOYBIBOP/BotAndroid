import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.a1_task_pogoda_compose.ui.data.ForecastDay
import com.example.a1_task_pogoda_compose.ui.theme.Typography
import com.example.a1_task_pogoda_compose.ui.ui.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    val weatherViewModel: WeatherViewModel = viewModel()
    val weatherDataState by weatherViewModel.weatherData.collectAsState()
    val weatherData = weatherDataState
    val showMenu = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    val showError = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(true) }
    val temperatureType = remember { mutableStateOf("C") }

    isLoading.value = weatherData.isEmpty()

    val backgroundColor by animateColorAsState(
        targetValue = if (!isLoading.value) {
            getBackgroundColor(weatherData[0].day.condition.text)
        } else {
            Color.White
        },
        animationSpec = tween(durationMillis = 1000)
    )

    AnimatedVisibility(
        visible = !isLoading.value,
        enter = fadeIn(animationSpec = tween(1000)),
        exit = fadeOut(animationSpec = tween(1000))
    ) {
        val location = weatherViewModel.location

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .background(backgroundColor),
            topBar = { MainTopBar(location, showDialog, showMenu, temperatureType) },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.Transparent)
            ) {
                CurrentTemperature(
                    if (temperatureType.value == "C") weatherData[0].day.avgtemp_c else weatherData[0].day.avgtemp_f,
                    weatherData[0].day.condition.text,
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.Transparent)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .background(Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = Color.Gray.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(16.dp)
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(weatherData) { forecastDay ->
                                WeatherDayItem(forecastDay)
                            }
                        }
                    }
                }
            }
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

@Composable
fun getBackgroundColor(description: String): Color {
    return when {
        "rain" in description.lowercase() -> Color.Blue
        "cloud" in description.lowercase() -> Color.LightGray
        "sun" in description.lowercase() -> Color.Yellow
        "snow" in description.lowercase() -> Color.White
        else -> Color.White
    }
}

@Composable
fun WeatherDayItem(forecastDay: ForecastDay) {
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
        Text(
            text = "${forecastDay.day.avgtemp_c.toInt()}°C / ${forecastDay.day.avgtemp_f.toInt()}°F",
            style = Typography.bodyLarge,
        )
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
    showMenu: MutableState<Boolean>,
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
            MainLoction(location)
        },
        navigationIcon = {
            IconButton(onClick = { showDialog.value = true }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
            }
        },
        actions = {
            Box {
                IconButton(onClick = { showMenu.value = true }) {
                    Icon(imageVector = Icons.Filled.List, contentDescription = "Menu")
                }
                DropdownMenu(
                    expanded = showMenu.value,
                    onDismissRequest = { showMenu.value = false },
                    modifier = Modifier
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                ) {
                    DropdownMenuItem(
                        text = { Text("Celsius") },
                        onClick = {
                            temperatureType.value = "C"
                            showMenu.value = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Fahrenheit") },
                        onClick = {
                            temperatureType.value = "F"
                            showMenu.value = false
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun MainLoction(location: String) {
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
