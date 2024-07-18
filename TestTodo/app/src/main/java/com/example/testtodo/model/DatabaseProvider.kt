import android.content.Context
import androidx.room.Room
import com.example.testtodo.db.TodoDatabase

object DatabaseProvider {
    private var INSTANCE: TodoDatabase? = null
    fun getDatabase(context: Context): TodoDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                TodoDatabase::class.java,
                "todo_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
