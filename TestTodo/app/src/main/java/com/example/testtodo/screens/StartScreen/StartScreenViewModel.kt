package com.example.testtodo.screens.StartScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.testtodo.db.TodoEntity

class StartScreenViewModel:ViewModel() {
    val searchTodo: MutableState<String> = mutableStateOf("")
    val showDialog:MutableState<Boolean> = mutableStateOf(false)
    val itemToDelete:MutableState<TodoEntity?> = mutableStateOf(null)

    fun showDialogValue(
        item: TodoEntity
    ){
        itemToDelete.value = item
        showDialog.value = true
    }
    fun hideDialogValue(){
        itemToDelete.value = null
        showDialog.value = false
    }
}