package com.example.notesroom.model

object TodoManager {
    private val todoList = mutableListOf<Todo>()
    fun gatAllTodo():List<Todo>{
        return todoList
    }

    fun addTodo(title: String, body:String){
        todoList.add(Todo(System.currentTimeMillis().toInt(), body, title))
    }
    fun deleteTodo(id:Int){
        todoList.removeIf{
            it.id==id
        }
    }
}