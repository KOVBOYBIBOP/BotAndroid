package com.example.testtodo.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ThemeViewModel : ViewModel() {
    val expanded = mutableStateOf(false)
    val themeOptions = listOf("System", "Dark", "Light")
    val selectedOption = mutableStateOf(themeOptions[0])

    var isDarkTheme = mutableStateOf<Boolean?>(null)
        private set

    fun updateThemeSelection(option: String) {
        selectedOption.value = option
        when (option) {
            "Dark" -> isDarkTheme.value = true
            "Light" -> isDarkTheme.value = false
            "System" -> isDarkTheme.value = null // null for system theme
        }
    }

    fun toggleTheme() {
        isDarkTheme.value = isDarkTheme.value?.not()
    }
}
