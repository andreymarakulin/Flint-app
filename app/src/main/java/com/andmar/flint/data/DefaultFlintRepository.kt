package com.andmar.flint.data

import com.andmar.flint.FlintActions
import com.andmar.flint.firebase.AuthItem
import com.andmar.flint.firebase.CategoryItem
import com.andmar.flint.firebase.LabelItem
import com.andmar.flint.firebase.NoteItem
import com.andmar.flint.firebase.ReminderItem
import com.andmar.flint.firebase.TodoItem
import com.andmar.flint.firebase.UserItem
import com.andmar.flint.ui.theme.todo.TodoDetails
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface DefaultFlintRepository {

    //Auth
    fun getCurrentUser(): FirebaseUser?
    fun getAuthState(): Flow<FlintActions>
    fun signIn(authItem: AuthItem, onFlintActions: (FlintActions) -> Unit)
    fun createUser(authItem: AuthItem, onFlintActions: (FlintActions) -> Unit)
    fun signOut(onFlintActions: (FlintActions) -> Unit)

    //User
    fun createUser(userItem: UserItem, onFlintActions: (FlintActions) -> Unit)

    //Category
    fun createCategory(categoryItem: CategoryItem, onFlintActions: (FlintActions) -> Unit)
    fun editCategory(categoryItem: CategoryItem, onFlintActions: (FlintActions) -> Unit)
    fun deleteCategory(categoryItem: CategoryItem, onFlintActions: (FlintActions) -> Unit)
    fun getCategoryById(categoryId: String): Flow<CategoryItem>
    fun getCategories(): Flow<List<CategoryItem>>


    //Note
    fun createNote(noteItem: NoteItem, onFlintActions: (FlintActions) -> Unit)
    fun editNote(noteItem: NoteItem, onFlintActions: (FlintActions) -> Unit)
    fun deleteNote(noteItem: NoteItem, onFlintActions: (FlintActions) -> Unit)
    fun getNoteById(noteId: String): Flow<NoteItem>
    fun getNotes(): Flow<List<NoteItem>>

    //Todo
    fun createTodo(todoItem: TodoItem, onFlintActions: (FlintActions) -> Unit)
    fun editTodo(todoItem: TodoItem, onFlintActions: (FlintActions) -> Unit)
    fun deleteTodo(todoItem: TodoItem, onFlintActions: (FlintActions) -> Unit)
    fun getTodoById(todoId: String): Flow<TodoItem>
    fun getTodos(): Flow<List<TodoItem>>

    //Label
    fun createLabel(labelItem: LabelItem, onFlintActions: (FlintActions) -> Unit)
    fun editLabel(labelItem: LabelItem, onFlintActions: (FlintActions) -> Unit)
    fun getLabels(): Flow<List<LabelItem>>

    //Reminder
    fun createReminder(reminderItem: ReminderItem, onFlintActions: (FlintActions) -> Unit)
    fun editReminder(reminderItem: ReminderItem, onFlintActions: (FlintActions) -> Unit)
    fun getReminders(): Flow<List<ReminderItem>>
}
