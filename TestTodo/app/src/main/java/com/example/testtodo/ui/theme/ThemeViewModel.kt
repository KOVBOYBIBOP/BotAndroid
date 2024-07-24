package com.example.testtodo.ui.theme

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ThemeViewModel : ViewModel() {
    var isDarkTheme = mutableStateOf(false)
        private set

    fun toggleTheme() {
        isDarkTheme.value = !isDarkTheme.value
    }
}
