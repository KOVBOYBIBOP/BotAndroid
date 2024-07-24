package com.example.testtodo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(version = 3, entities = [TodoEntity::class, Category::class])
abstract class TodoDatabase : RoomDatabase() {
    abstract fun getTodoDao(): TodoDao

    companion object {
        @Volatile
        internal var INSTANCE: TodoDatabase? = null

        internal val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Создаем таблицу Category
                database.execSQL("""
            CREATE TABLE IF NOT EXISTS Category (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL
            )
        """.trimIndent())

                // Создаем новую таблицу TodoItem с nullable categoryId
                database.execSQL("""
            CREATE TABLE IF NOT EXISTS new_TodoItem (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                title TEXT NOT NULL,
                description TEXT NOT NULL,
                categoryId INTEGER,
                FOREIGN KEY(categoryId) REFERENCES Category(id) ON DELETE CASCADE
            )
        """.trimIndent())

                // Переносим данные из старой таблицы в новую
                database.execSQL("""
            INSERT INTO new_TodoItem (id, title, description, categoryId)
            SELECT id, title, description, NULL FROM TodoItem
        """.trimIndent())

                // Удаляем старую таблицу и переименовываем новую
                database.execSQL("DROP TABLE TodoItem")
                database.execSQL("ALTER TABLE new_TodoItem RENAME TO TodoItem")
            }
        }

    }
}