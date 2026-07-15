package com.andmar.flint.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FlintDao {

    //Category
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategoryItem(categoryItem: CategoryItem)
    @Update
    suspend fun updateCategoryItem(categoryItem: CategoryItem)
    @Delete
    suspend fun deleteCategoryItem(categoryItem: CategoryItem)
    @Query("SELECT * FROM category_item WHERE id = :categoryId")
    fun getCategoryItemById(categoryId: String): Flow<CategoryItem>
    @Query("SELECT * FROM category_item")
    fun getCategoryItems(): Flow<List<CategoryItem>>

    //Note
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNoteItem(noteItem: NoteItem)
    @Update
    suspend fun updateNoteItem(noteItem: NoteItem)
    @Delete
    suspend fun deleteNoteItem(noteItem: NoteItem)
    @Query("SELECT * FROM note_item WHERE id = :noteId")
    fun getNoteItemById(noteId: String): Flow<NoteItem>
    @Query("SELECT * FROM note_item")
    fun getNoteItems(): Flow<List<NoteItem>>

    //Todo
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTodoItem(todoItem: TodoItem)
    @Update
    suspend fun updateTodoItem(todoItem: TodoItem)
    @Delete
    suspend fun deleteTodoItem(todoItem: TodoItem)
    @Query("SELECT * FROM todo_item WHERE id = :todoId")
    fun getTodoItemById(todoId: String): Flow<TodoItem>
    @Query("SELECT * FROM todo_item")
    fun getTodoItems(): Flow<List<TodoItem>>

    //Label
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLabelItem(labelItem: LabelItem)
    @Update
    suspend fun updateLabelItem(labelItem: LabelItem)
    @Delete
    suspend fun deleteLabelItem(labelItem: LabelItem)
    @Query("SELECT * FROM label_item WHERE id = :labelId")
    fun getLabelItemById(labelId: String): Flow<LabelItem>
    @Query("SELECT * FROM label_item")
    fun getLabelItems(): Flow<List<LabelItem>>

    //Reminder
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReminderItem(reminderItem: ReminderItem)
    @Update
    suspend fun updateReminderItem(reminderItem: ReminderItem)
    @Delete
    suspend fun deleteReminderItem(reminderItem: ReminderItem)
    @Query("SELECT * FROM reminder_item WHERE id = :reminderId")
    fun getReminderItemById(reminderId: String): Flow<ReminderItem>
    @Query("SELECT * FROM reminder_item")
    fun getReminderItems(): Flow<List<ReminderItem>>
}