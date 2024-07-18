// TodoViewModel.kt
package com.example.notesroom.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notesroom.db.dao.TodoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val todoDao = TodoDatabase.getInstance(application).getTodoDao()
    val todolist: LiveData<List<Todo>> = todoDao.getAllTodo()

    fun getAllTodo():LiveData<List<Todo>> {
        return todolist
    }

    fun addTodo(title: String, body: String) {
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.addTodo(Todo(0, body, title))
        }
    }

    fun deleteTodoById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.deleteTodo(id)
        }
    }
}
