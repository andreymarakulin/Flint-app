package ru.andmar.flint.core.data

import kotlinx.coroutines.flow.Flow
import ru.andmar.flint.features.account.data.model.UserItem
import ru.andmar.flint.features.category.data.model.CategoryItem
import ru.andmar.flint.features.label.data.model.LabelItem
import ru.andmar.flint.features.note.data.model.NoteItem
import ru.andmar.flint.features.reminder.data.model.ReminderItem
import ru.andmar.flint.features.todo.data.model.TodoItem

interface DefaultFirestoreClient {

    //User
    suspend fun setUserItem()
    suspend fun getUserItem(): UserItem

    //Category
    suspend fun setCategoryItem(categoryItem: CategoryItem)
    suspend fun deleteCategoryItem(categoryId: String)
    suspend fun getCategoryItemOnce(categoryId: String): CategoryItem
    fun getCategoryItem(categoryId: String): Flow<CategoryItem>
    fun getCategoryItems(): Flow<List<CategoryItem>>

    //Note
    suspend fun setNoteItem(noteItem: NoteItem)
    suspend fun deleteNoteItem(noteId: String)
    suspend fun getNoteItemOnce(noteId: String): NoteItem
    fun getNoteItem(noteId: String): Flow<NoteItem>
    fun getNoteItems(): Flow<List<NoteItem>>

    //Todo
    suspend fun setTodoItem(todoItem: TodoItem)
    suspend fun deleteTodoItem(todoId: String)
    suspend fun getTodoItemOnce(todoId: String): TodoItem
    fun getTodoItem(todoId: String): Flow<TodoItem>
    fun getTodoItems(): Flow<List<TodoItem>>

    //Label
    suspend fun setLabelItem(labelItem: LabelItem)
    suspend fun deleteLabelItem(labelId: String)
    suspend fun getLabelItemOnce(labelId: String): LabelItem
    fun getLabelItem(labelId: String): Flow<LabelItem>
    fun getLabelItems(): Flow<List<LabelItem>>

    //Reminder
    suspend fun setReminderItem(reminderItem: ReminderItem)
    suspend fun deleteReminderItem(reminderId: String)
    suspend fun updateReminderDoneState(reminderId: String)
    suspend fun getReminderItemOnce(reminderId: String): ReminderItem
    fun getReminderItem(reminderId: String): Flow<ReminderItem>
    fun getReminderItems(): Flow<List<ReminderItem>>
}