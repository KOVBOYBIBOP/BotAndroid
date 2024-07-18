package com.example.testtodo.model

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.testtodo.db.TodoDao
import com.example.testtodo.db.TodoEntity

class RepositoryTodo(private val todoDao: TodoDao) {
    val allTodos: LiveData<List<TodoEntity>> = todoDao.getAllTodo()

    suspend fun insert(todo: TodoEntity) {
        todoDao.insertTodo(todo)
    }

    fun update(todo: TodoEntity) {
        todoDao.updateTodo(todo)
    }


    suspend fun delete(todo: TodoEntity) {
        todoDao.deleteTodo(todo)
    }
}
