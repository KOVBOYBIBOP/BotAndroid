package com.example.testtodo.screens.AddScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class AddViewScreenModel : ViewModel() {

    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")


    fun clearFields() {
        title.value = ""
        description.value = ""
    }
}
