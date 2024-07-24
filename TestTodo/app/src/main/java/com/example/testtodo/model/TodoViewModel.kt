import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.testtodo.db.Category
import com.example.testtodo.db.TodoEntity
import com.example.testtodo.model.RepositoryTodo
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RepositoryTodo
    val allTodos: LiveData<List<TodoEntity>>
    val allCategories: LiveData<List<Category>>

    init {
        val todoDao = DatabaseProvider.getDatabase(application).getTodoDao()
        repository = RepositoryTodo(todoDao)
        allTodos = repository.allTodos
        allCategories = repository.allCategories
    }

    fun insertTodo(todo: TodoEntity) = viewModelScope.launch {
        repository.insertTodo(todo)
    }

    fun getTodoById(id: Int): LiveData<TodoEntity?> {
        return allTodos.map { todos -> todos.find { it.id == id } }
    }

    fun deleteTodo(todo: TodoEntity) = viewModelScope.launch {
        repository.deleteTodo(todo)
    }

    fun deleteCategory(category: Category) = viewModelScope.launch {
        repository.deleteCategory(category)
    }

    fun insertTCategory(category: Category) = viewModelScope.launch {
        repository.insertCategory(category)
    }

    fun getCategoryByName(name: String): LiveData<Category?> {
        return allCategories.map { categories -> categories.find { it.name == name } }
    }
}


