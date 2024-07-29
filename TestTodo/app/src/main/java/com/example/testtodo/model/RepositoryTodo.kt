package com.example.testtodo.model

import androidx.lifecycle.LiveData
import com.example.testtodo.db.Category
import com.example.testtodo.db.TodoCategoryCrossRef
import com.example.testtodo.db.TodoDao
import com.example.testtodo.db.TodoEntity
import com.example.testtodo.db.TodoWithCategories
import com.example.testtodo.db.CategoryWithTodos

class RepositoryTodo(private val todoDao: TodoDao) {

    // Получение всех задач и категорий
    val allTodos: LiveData<List<TodoEntity>> = todoDao.getAllTodo()
    val allCategories: LiveData<List<Category>> = todoDao.getAllCategory()

    // Вставка и удаление задач
    suspend fun insertTodo(todo: TodoEntity) {
        todoDao.insertTodo(todo)
    }

    suspend fun updateTodo(todo: TodoEntity) {
        todoDao.updateTodo(todo)
    }

    suspend fun deleteTodo(todo: TodoEntity) {
        todoDao.deleteTodo(todo)
    }

    // Вставка и удаление категорий
    suspend fun insertCategory(category: Category) {
        todoDao.insertCategory(category)
    }

    suspend fun updateCategory(category: Category) {
        todoDao.updateCategory(category)
    }

    suspend fun deleteCategory(category: Category) {
        todoDao.deleteCategory(category)
    }

    // Работа с промежуточной таблицей
    suspend fun insertTodoCategoryCrossRef(crossRef: TodoCategoryCrossRef) {
        todoDao.insertTodoCategoryCrossRef(crossRef)
    }

    suspend fun deleteTodoCategoryCrossRef(crossRef: TodoCategoryCrossRef) {
        todoDao.deleteTodoCategoryCrossRef(crossRef)
    }

    // Получение задач с категориями
    fun getTodoWithCategories(todoId: Int): LiveData<TodoWithCategories> {
        return todoDao.getTodoWithCategories(todoId)
    }

    // Получение категорий с задачами
    fun getCategoryWithTodos(categoryId: Int): LiveData<CategoryWithTodos> {
        return todoDao.getCategoryWithTodos(categoryId)
    }
}
