package com.andmar.flint.data

import android.os.Build
import com.andmar.flint.FlintActions
import com.andmar.flint.firebase.AuthItem
import com.andmar.flint.firebase.CategoryItem
import com.andmar.flint.firebase.DefaultAuthClient
import com.andmar.flint.firebase.DefaultFirestoreClient
import com.andmar.flint.firebase.LabelItem
import com.andmar.flint.firebase.NoteItem
import com.andmar.flint.firebase.ReminderItem
import com.andmar.flint.firebase.TodoItem
import com.andmar.flint.firebase.UserItem
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

class FlintRepository(
    private val authClient: DefaultAuthClient,
    private val firestoreClient: DefaultFirestoreClient
): DefaultFlintRepository {

    //Auth
    override fun getCurrentUser(): FirebaseUser? =
        authClient.getCurrentUser()

    override fun getAuthState(): Flow<FlintActions> =
        authClient.getAuthState()

    override fun signIn(
        authItem: AuthItem,
        onFlintActions: (FlintActions) -> Unit
    ) = authClient.signIn(authItem, onFlintActions)

    override fun createUser(
        authItem: AuthItem,
        onFlintActions: (FlintActions) -> Unit
    ) = authClient.createUser(authItem, onFlintActions)

    override fun signOut(onFlintActions: (FlintActions) -> Unit) =
        authClient.signOut(onFlintActions)

    //User
    override fun createUser(
        userItem: UserItem,
        onFlintActions: (FlintActions) -> Unit
    ) = firestoreClient.setUserItem(userItem, onFlintActions)

    //Category
    override fun createCategory(
        categoryItem: CategoryItem,
        onFlintActions: (FlintActions) -> Unit
    ) = firestoreClient.addCategoryItem(categoryItem, onFlintActions)

    override fun editCategory(
        categoryItem: CategoryItem,
        onFlintActions: (FlintActions) -> Unit
    ) = firestoreClient.setCategoryItem(categoryItem, onFlintActions)

    override fun deleteCategory(
        categoryItem: CategoryItem,
        onFlintActions: (FlintActions) -> Unit
    ) = firestoreClient.deleteCategoryItem(categoryItem, onFlintActions)

    override fun getCategoryById(categoryId: String): Flow<CategoryItem> =
        firestoreClient.getCategoryItem(categoryId)

    override fun getCategories(): Flow<List<CategoryItem>> =
        firestoreClient.getCategoryItems()

    //Note
    override fun createNote(
        noteItem: NoteItem,
        onFlintActions: (FlintActions) -> Unit
    ) = firestoreClient.addNoteItem(noteItem, onFlintActions)

    override fun editNote(
        noteItem: NoteItem,
        onFlintActions: (FlintActions) -> Unit
    ) = firestoreClient.setNoteItem(noteItem, onFlintActions)

    override fun deleteNote(
        noteItem: NoteItem,
        onFlintActions: (FlintActions) -> Unit
    ) = firestoreClient.deleteNoteItem(noteItem, onFlintActions)

    override fun getNoteById(noteId: String): Flow<NoteItem> =
        firestoreClient.getNoteItem(noteId)

    override fun getNotes(): Flow<List<NoteItem>> =
        firestoreClient.getNoteItems()

    //Todo
    override fun createTodo(
        todoItem: TodoItem,
        onFlintActions: (FlintActions) -> Unit
    ) = firestoreClient.addTodoItem(todoItem, onFlintActions)

    override fun editTodo(
        todoItem: TodoItem,
        onFlintActions: (FlintActions) -> Unit
    ) = firestoreClient.setTodoItem(todoItem, onFlintActions)

    override fun deleteTodo(
        todoItem: TodoItem,
        onFlintActions: (FlintActions) -> Unit
    ) = firestoreClient.deleteTodoItem(todoItem, onFlintActions)

    override fun getTodoById(todoId: String): Flow<TodoItem> =
        firestoreClient.getTodoItem(todoId)

    override fun getTodos(): Flow<List<TodoItem>> =
        firestoreClient.getTodoItems()

    //Label
    override fun createLabel(
        labelItem: LabelItem,
        onFlintActions: (FlintActions) -> Unit
    ) = firestoreClient.addLabelItem(labelItem, onFlintActions)

    override fun editLabel(
        labelItem: LabelItem,
        onFlintActions: (FlintActions) -> Unit
    ) = firestoreClient.setLabelItem(labelItem, onFlintActions)

    override fun getLabels(): Flow<List<LabelItem>> =
        firestoreClient.getLabelItems()

    //Reminder
    override fun createReminder(
        reminderItem: ReminderItem,
        onFlintActions: (FlintActions) -> Unit
    ) = firestoreClient.addReminderItem(reminderItem, onFlintActions)

    override fun editReminder(
        reminderItem: ReminderItem,
        onFlintActions: (FlintActions) -> Unit
    ) = firestoreClient.setReminderItem(reminderItem, onFlintActions)

    override fun getReminders(): Flow<List<ReminderItem>> =
        firestoreClient.getReminderItems()
}