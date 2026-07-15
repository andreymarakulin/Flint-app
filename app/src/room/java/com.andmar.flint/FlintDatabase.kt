package com.andmar.flint.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

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