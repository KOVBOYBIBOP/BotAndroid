import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.testtodo.db.Category
import com.example.testtodo.db.CategoryWithTodos
import com.example.testtodo.db.TodoCategoryCrossRef
import com.example.testtodo.db.TodoEntity
import com.example.testtodo.db.TodoWithCategories
import com.example.testtodo.model.DatabaseProvider
import com.example.testtodo.model.RepositoryTodo
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RepositoryTodo
    val allTodos: LiveData<List<TodoEntity>>
    val allCategories: LiveData<List<Category>>

    init {
        val todoDao = DatabaseProvider.getDatabase(application).todoDao()
        repository = RepositoryTodo(todoDao)
        allTodos = repository.allTodos
        allCategories = repository.allCategories
    }

    // CRUD операции для задач
    fun insertTodo(todo: TodoEntity) = viewModelScope.launch {
        repository.insertTodo(todo)
    }

    fun updateTodo(todo: TodoEntity) = viewModelScope.launch {
        repository.updateTodo(todo)
    }

    fun deleteTodo(todo: TodoEntity) = viewModelScope.launch {
        repository.deleteTodo(todo)
    }

    fun getTodoById(id: Int): LiveData<TodoEntity?> {
        return allTodos.map{ todos -> todos.find { it.todoId == id } }
    }

    fun getCategoryById(id: Int): LiveData<Category?> {
        return allCategories.map{ categories -> categories.find { it.categoryId == id } }
    }

    // CRUD операции для категорий
    fun insertCategory(category: Category) = viewModelScope.launch {
        repository.insertCategory(category)
    }

    fun updateCategory(category: Category) = viewModelScope.launch {
        repository.updateCategory(category)
    }

    fun deleteCategory(category: Category) = viewModelScope.launch {
        repository.deleteCategory(category)
    }

    fun getCategoryByName(name: String): LiveData<Category?> {
        return allCategories.map { categories -> categories.find { it.name == name } }
    }

    // Работа с промежуточной таблицей
    fun insertTodoCategoryCrossRef(todoId: Int, categoryId: Int) = viewModelScope.launch {
        val crossRef = TodoCategoryCrossRef(todoId, categoryId)
        repository.insertTodoCategoryCrossRef(crossRef)
    }

    fun deleteTodoCategoryCrossRef(todoId: Int, categoryId: Int) = viewModelScope.launch {
        val crossRef = TodoCategoryCrossRef(todoId, categoryId)
        repository.deleteTodoCategoryCrossRef(crossRef)
    }

    // Получение задач с категориями
    fun getTodoWithCategories(todoId: Int): LiveData<TodoWithCategories> {
        return repository.getTodoWithCategories(todoId)
    }

    // Получение категорий с задачами
    fun getCategoryWithTodos(categoryId: Int): LiveData<CategoryWithTodos> {
        return repository.getCategoryWithTodos(categoryId)
    }
}
