package com.example.testtodo.model

import androidx.lifecycle.LiveData
import com.example.testtodo.db.Category
import com.example.testtodo.db.TodoDao
import com.example.testtodo.db.TodoEntity

class RepositoryTodo(private val todoDao: TodoDao) {
    val allTodos: LiveData<List<TodoEntity>> = todoDao.getAllTodo()
    val allCategories:LiveData<List<Category>> = todoDao.getAllCategory()

    suspend fun insertTodo(todo: TodoEntity) {
        todoDao.insertTodo(todo)
    }

    suspend fun deleteTodo(todo: TodoEntity) {
        todoDao.deleteTodo(todo)
    }

    suspend fun insertCategory(category: Category) {
        todoDao.insertCategory(category)
    }

    suspend fun deleteCategory(category: Category) {
        todoDao.deleteCategory(category)
    }
}
