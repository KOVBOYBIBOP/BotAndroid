// StartViewModel.kt
package com.example.notesroom.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.notesroom.model.Todo

@RequiresApi(Build.VERSION_CODES.O)
class StartViewModel : ViewModel() {
    var taskTitle by mutableStateOf("")
    private var _searchQuery by mutableStateOf("")

    private val _offsetX = Animatable(0f)
    val offsetX: Float
        get() = _offsetX.value

    suspend fun setOffsetX(value: Float) {
        _offsetX.snapTo(value)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllTodo(): List<Todo> {
        getAllTodo()
    }

    fun setSearchQuery(query: String) {
        _searchQuery = query
    }

    fun clearSearchQuery() {
        _searchQuery = ""
    }

    fun deleteItem(item: Todo) {
        // Logic to delete item
    }
}
