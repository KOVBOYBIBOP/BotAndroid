import android.content.Context
import androidx.room.Room
import com.example.testtodo.db.TodoDatabase

object DatabaseProvider {
    fun getDatabase(context: Context): TodoDatabase {
        return TodoDatabase.INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                TodoDatabase::class.java,
                "todo_database"
            )
                .addMigrations(TodoDatabase.MIGRATION_2_3)
                .build()
            TodoDatabase.INSTANCE = instance
            instance
        }
    }
}
