package com.example.testtodo.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E)
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE)
)


@Composable
fun TestTODOTheme(
    themeViewModel: ThemeViewModel = viewModel(),
    content: @Composable () -> Unit
) {
    val isSystemDarkTheme = isSystemInDarkTheme()
    val isDarkTheme by rememberUpdatedState(themeViewModel.isDarkTheme.value)

    val colors = when {
        isDarkTheme == null -> if (isSystemDarkTheme) DarkColorScheme else LightColorScheme
        isDarkTheme!! -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}



