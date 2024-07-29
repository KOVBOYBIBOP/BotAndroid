package com.example.testtodo.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: TodoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoCategoryCrossRef(crossRef: TodoCategoryCrossRef)

    @Delete
    suspend fun deleteTodo(todo: TodoEntity)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Delete
    suspend fun deleteTodoCategoryCrossRef(crossRef: TodoCategoryCrossRef)

    @Update
    suspend fun updateTodo(todo: TodoEntity)

    @Update
    suspend fun updateCategory(category: Category)

    @Query("SELECT * FROM TodoItem")
    fun getAllTodo(): LiveData<List<TodoEntity>>

    @Query("SELECT * FROM Category")
    fun getAllCategory(): LiveData<List<Category>>

    @Transaction
    @Query("SELECT * FROM TodoItem WHERE todoId = :todoId")
    fun getTodoWithCategories(todoId: Int): LiveData<TodoWithCategories>

    @Transaction
    @Query("SELECT * FROM Category WHERE categoryId = :categoryId")
    fun getCategoryWithTodos(categoryId: Int): LiveData<CategoryWithTodos>
}

