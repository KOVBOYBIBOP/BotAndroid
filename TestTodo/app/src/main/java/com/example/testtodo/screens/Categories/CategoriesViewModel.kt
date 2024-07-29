package com.example.testtodo.screens.Categories

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.testtodo.db.TodoEntity

class CategoriesViewModel:ViewModel() {

    val expanded = mutableStateOf(false)
    val colors = listOf(Color.Black,Color.Blue,Color.Red, Color.Green)
    val name: MutableState<String> = mutableStateOf("")
    val color: MutableState<Color> = mutableStateOf(Color.Black)



    val showDialog: MutableState<Boolean> = mutableStateOf(false)
    val itemToDelete: MutableState<TodoEntity?> = mutableStateOf(null)
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