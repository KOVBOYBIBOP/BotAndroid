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

    private var _searchQuery by mutableStateOf("")


    var taskTitle by mutableStateOf("")



    private val _offsetX = Animatable(0f)
    val offsetX: Float
        get() = _offsetX.value

    suspend fun setOffsetX(value: Float) {
        _offsetX.snapTo(value)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFakeTodo(): List<Todo> {
        // Mock data generation
        return listOf(
            Todo(1, "First todo", "Home"),
            Todo(2, "Second todo", "Home"),
            Todo(3, "Third todo", "Home"),
            Todo(4, "Fourth todo", "Home"),
            Todo(3, "Third todo", "Home"),
            Todo(3, "Third todo", "Home"),
            Todo(3, "Third todo", "Home"),
            Todo(3, "Third todo", "Home"),
            Todo(3, "Third todo", "Home"),
            Todo(3, "Third todo", "Home"),
            Todo(3, "Third todo", "Home"),

        ).filter {
            it.body.contains(_searchQuery, ignoreCase = true)
        }
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
