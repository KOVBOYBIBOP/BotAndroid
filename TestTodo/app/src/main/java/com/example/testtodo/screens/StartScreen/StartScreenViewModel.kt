package com.example.testtodo.screens.StartScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class StartScreenViewModel:ViewModel() {
    val searchTodo: MutableState<String> = mutableStateOf("")


}