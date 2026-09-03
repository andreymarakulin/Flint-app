package ru.andmar.flint.core.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.andmar.flint.features.category.data.model.CategoryItem
import ru.andmar.flint.features.label.data.model.LabelItem
import ru.andmar.flint.features.note.data.model.NoteItem
import ru.andmar.flint.features.reminder.data.model.ReminderItem
import ru.andmar.flint.features.todo.data.model.TodoItem

@Database(
    entities = [
        CategoryItem::class,
        NoteItem::class,
        TodoItem::class,
        LabelItem::class,
        ReminderItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FlintDatabase: RoomDatabase() {
    abstract fun flintDao(): FlintDao

    companion object {
        @Volatile
        var Instance: FlintDatabase? = null

        fun getDatabase(context: Context): FlintDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, FlintDatabase::class.java, "Flint Database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}