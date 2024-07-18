package com.example.notesroom.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true)
    var id: Int =0,
    var body : String,
    var title : String
)

