package com.example.notesroom.screens.addnote

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AddNoteViewModel : ViewModel() {
    var taskTitle by mutableStateOf("")
    var taskDescription by mutableStateOf("")

    // Функция для сохранения задачи (по вашему описанию, пока что она пустая)
    fun saveTask() {
        // Логика сохранения задачи может быть добавлена позже
    }
}
