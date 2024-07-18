// AddNoteViewModel.kt
package com.example.notesroom.screens.addnote

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesroom.db.dao.TodoDatabase
import com.example.notesroom.model.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddNoteViewModel(application: Application) : AndroidViewModel(application) {
    var taskTitle by mutableStateOf("")
    var taskDescription by mutableStateOf("")
    private val todoDao = TodoDatabase.getInstance(application).getTodoDao()

    fun saveTask(title: String, body: String) {
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.addTodo(Todo(0, body, title))
        }
    }
}
