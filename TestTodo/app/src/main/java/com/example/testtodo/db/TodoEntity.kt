package com.example.testtodo.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TodoItem")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
)
