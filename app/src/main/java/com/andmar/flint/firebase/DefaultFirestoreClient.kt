package com.andmar.flint.firebase

import com.andmar.flint.FlintActions
import kotlinx.coroutines.flow.Flow

interface DefaultFirestoreClient {

    //User
    fun setUserItem(userItem: UserItem, onFlintActions: (FlintActions) -> Unit)

    //Category
    fun addCategoryItem(categoryItem: CategoryItem, onFlintActions: (FlintActions) -> Unit)
    fun setCategoryItem(categoryItem: CategoryItem, onFlintActions: (FlintActions) -> Unit)
    fun deleteCategoryItem(categoryItem: CategoryItem, onFlintActions: (FlintActions) -> Unit)
    fun getCategoryItem(categoryId: String): Flow<CategoryItem>
    fun getCategoryItems(): Flow<List<CategoryItem>>

    //Note
    fun addNoteItem(noteItem: NoteItem, onFlintActions: (FlintActions) -> Unit)
    fun setNoteItem(noteItem: NoteItem, onFlintActions: (FlintActions) -> Unit)
    fun deleteNoteItem(noteItem: NoteItem, onFlintActions: (FlintActions) -> Unit)
    fun getNoteItem(noteId: String): Flow<NoteItem>
    fun getNoteItems(): Flow<List<NoteItem>>

    //Todo
    fun addTodoItem(todoItem: TodoItem, onFlintActions: (FlintActions) -> Unit)
    fun setTodoItem(todoItem: TodoItem, onFlintActions: (FlintActions) -> Unit)
    fun deleteTodoItem(todoItem: TodoItem, onFlintActions: (FlintActions) -> Unit)
    fun getTodoItem(todoId: String): Flow<TodoItem>
    fun getTodoItems(): Flow<List<TodoItem>>

    //Label
    fun addLabelItem(labelItem: LabelItem, onFlintActions: (FlintActions) -> Unit)
    fun setLabelItem(labelItem: LabelItem, onFlintActions: (FlintActions) -> Unit)
    fun getLabelItem(labelId: String): Flow<LabelItem>
    fun getLabelItems(): Flow<List<LabelItem>>

    //Reminder
    fun addReminderItem(reminderItem: ReminderItem, onFlintActions: (FlintActions) -> Unit)
    fun setReminderItem(reminderItem: ReminderItem, onFlintActions: (FlintActions) -> Unit)
    fun getReminderItem(reminderId: String): Flow<ReminderItem>
    fun getReminderItems(): Flow<List<ReminderItem>>

}