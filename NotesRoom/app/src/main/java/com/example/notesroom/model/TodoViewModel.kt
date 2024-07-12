package com.example.notesroom.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TodoViewModel:ViewModel() {
    private var _todolist = MutableLiveData<List<Todo>>()
    val todolist: LiveData<List<Todo>> = _todolist

    fun getAllTodo(): List<Todo> {

    }

    fun addTodo() {

    }

    fun deleteTodo(){

    }

}