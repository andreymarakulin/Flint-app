package com.andmar.flint

import kotlinx.coroutines.flow.Flow

interface DefaultFirestoreClient {

    //User
    suspend fun setUserItem(userItem: UserItem)

    //Category
    suspend fun addCategoryItem(categoryItem: CategoryItem)
    suspend fun setCategoryItem(categoryItem: CategoryItem)
    suspend fun deleteCategoryItem(categoryId: String)
    fun getCategoryItem(categoryId: String): Flow<CategoryItem>
    fun getCategoryItems(): Flow<List<CategoryItem>>

    //Note
    suspend fun addNoteItem(noteItem: NoteItem)
    suspend fun setNoteItem(noteItem: NoteItem)
    suspend fun deleteNoteItem(noteId: String)
    fun getNoteItem(noteId: String): Flow<NoteItem>
    fun getNoteItems(): Flow<List<NoteItem>>

    //Todo
    suspend fun addTodoItem(todoItem: TodoItem)
    suspend fun setTodoItem(todoItem: TodoItem)
    suspend fun deleteTodoItem(todoId: String)
    fun getTodoItem(todoId: String): Flow<TodoItem>
    fun getTodoItems(): Flow<List<TodoItem>>

    //Label
    suspend fun addLabelItem(labelItem: LabelItem)
    suspend fun setLabelItem(labelItem: LabelItem)
    suspend fun deleteLabelItem(labelId: String)
    fun getLabelItem(labelId: String): Flow<LabelItem>
    fun getLabelItems(): Flow<List<LabelItem>>

    //Reminder
    suspend fun addReminderItem(reminderItem: ReminderItem)
    suspend fun setReminderItem(reminderItem: ReminderItem)
    suspend fun deleteReminderItem(reminderId: String)
    fun getReminderItem(reminderId: String): Flow<ReminderItem>
    fun getReminderItems(): Flow<List<ReminderItem>>
}