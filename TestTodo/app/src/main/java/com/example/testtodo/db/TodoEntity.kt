package com.example.testtodo.db

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

// Таблица для Todo
@Entity(tableName = "TodoItem")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val todoId: Int = 0,
    val title: String,
    val description: String
)

@Entity(tableName = "Category")
data class Category(
    @PrimaryKey(autoGenerate = true) val categoryId: Int = 0,
    val name: String,
    val color: Int // Используйте Int для хранения цвета
)



@Entity(
    tableName = "TodoCategoryCrossRef",
    primaryKeys = ["todoId", "categoryId"],
)
data class TodoCategoryCrossRef(
    val todoId: Int,
    val categoryId: Int
)



// Класс для Todo с категориями
data class TodoWithCategories(
    @Embedded val todo: TodoEntity,
    @Relation(
        parentColumn = "todoId",
        entityColumn = "categoryId",
        associateBy = Junction(TodoCategoryCrossRef::class)
    )
    val categories: List<Category> // Список категорий для данного Todo
)

// Класс для категории с Todo
data class CategoryWithTodos(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "todoId",
        associateBy = Junction(TodoCategoryCrossRef::class)
    )
    val todos: List<TodoEntity> // Список задач для данной категории
)


