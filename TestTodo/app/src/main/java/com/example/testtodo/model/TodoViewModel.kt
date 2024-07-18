import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.testtodo.db.TodoEntity
import com.example.testtodo.model.RepositoryTodo
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RepositoryTodo
    val allTodos: LiveData<List<TodoEntity>>

    init {
        val todoDao = DatabaseProvider.getDatabase(application).getTodoDao()
        repository = RepositoryTodo(todoDao)
        allTodos = repository.allTodos
    }

    fun insert(todo: TodoEntity) = viewModelScope.launch {
        repository.insert(todo)
    }


    fun getTodoById(id: Int): LiveData<TodoEntity?> {
        return allTodos.map { todos -> todos.find { it.id == id } }
    }


    fun delete(todo: TodoEntity) = viewModelScope.launch {
        repository.delete(todo)
    }
}
