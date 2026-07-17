package com.andmar.flint

import android.util.Log
import com.andmar.flint.ui.theme.account.AuthDetails
import com.andmar.flint.ui.theme.category.CategoryDetails
import com.andmar.flint.ui.theme.label.LabelDetails
import com.andmar.flint.ui.theme.note.NoteDetails
import com.andmar.flint.ui.theme.reminder.ReminderDetails
import com.andmar.flint.ui.theme.todo.TodoDetails
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FlintRepository(
    private val authClient: DefaultAuthClient,
    private val firestoreClient: DefaultFirestoreClient
) {

    //Auth
    fun getCurrentUser(): FirebaseUser? =
        authClient.getCurrentUser()

    fun getAuthState(): Flow<FlintActions> =
        authClient.getAuthState()

    suspend fun signIn(authDetails: AuthDetails) = authClient.signIn(authDetails.toAuthItem())

    suspend fun createUser(authDetails: AuthDetails) = authClient.createUser(authDetails.toAuthItem())

    suspend fun signOut() = authClient.signOut()

    //User
    suspend fun createUser(userItem: UserItem) = firestoreClient.setUserItem(userItem)
    //Category
    suspend fun createCategory(categoryDetails: CategoryDetails) = firestoreClient.addCategoryItem(categoryDetails.toCreateCategoryItem())
    suspend fun editCategory(categoryDetails: CategoryDetails) = firestoreClient.setCategoryItem(categoryDetails.toCategoryItem())
    suspend fun deleteCategory(categoryDetails: CategoryDetails) = firestoreClient.deleteCategoryItem(categoryDetails.id)
    fun getCategoryById(categoryId: String): Flow<CategoryDetails> =
        firestoreClient.getCategoryItem(categoryId).map { categoryItem -> categoryItem.toCategoryDetails() }
    fun getCategories(): Flow<List<CategoryDetails>> =
        firestoreClient.getCategoryItems().map { categoryItems ->
            categoryItems.map { categoryItem -> categoryItem.toCategoryDetails() }
        }
    //Note
    suspend fun createNote(noteDetails: NoteDetails) = firestoreClient.addNoteItem(noteDetails.toCreateNoteItem())
    suspend fun editNote(noteDetails: NoteDetails) = firestoreClient.setNoteItem(noteDetails.toNoteItem())
    suspend fun deleteNote(noteDetails: NoteDetails) = firestoreClient.deleteNoteItem(noteDetails.id)
    fun getNoteById(noteId: String): Flow<NoteDetails> =
        firestoreClient.getNoteItem(noteId).map { noteItem -> noteItem.toNoteDetails() }
    fun getNotes(): Flow<List<NoteDetails>> =
        firestoreClient.getNoteItems().map { noteItems ->
            Log.i("Note items", noteItems.toString())
            noteItems.map { noteItem -> noteItem.toNoteDetails() }
        }

    //Todo
    suspend fun createTodo(todoDetails: TodoDetails) = firestoreClient.addTodoItem(todoDetails.toCreateTodoItem())
    suspend fun editTodo(todoDetails: TodoDetails) = firestoreClient.setTodoItem(todoDetails.toTodoItem())
    suspend fun deleteTodo(todoDetails: TodoDetails) = firestoreClient.deleteTodoItem(todoDetails.id)
    fun getTodoById(todoId: String): Flow<TodoDetails> =
        firestoreClient.getTodoItem(todoId).map { todoItem -> todoItem.toTodoDetails() }
    fun getTodos(): Flow<List<TodoDetails>> =
        firestoreClient.getTodoItems().map { todoItems ->
            todoItems.map { todoItem -> todoItem.toTodoDetails() }
        }
    //Label
    suspend fun createLabel(labelDetails: LabelDetails) = firestoreClient.addLabelItem(labelDetails.toLabelItem())
    suspend fun editLabel(labelDetails: LabelDetails) = firestoreClient.setLabelItem(labelDetails.toLabelItem())
    suspend fun deleteLabel(labelDetails: LabelDetails) = firestoreClient.deleteLabelItem(labelDetails.id)
    fun getLabels(): Flow<List<LabelDetails>> =
        firestoreClient.getLabelItems().map { labelItems ->
            labelItems.map { labelItem -> labelItem.toLabelDetails() }
        }
    //Reminder
    suspend fun createReminder(reminderDetails: ReminderDetails) = firestoreClient.addReminderItem(reminderDetails.toReminderItem())
    suspend fun editReminder(reminderDetails: ReminderDetails) = firestoreClient.setReminderItem(reminderDetails.toReminderItem())
    suspend fun deleteReminder(reminderDetails: ReminderDetails) = firestoreClient.deleteReminderItem(reminderDetails.id)
    fun getReminders(): Flow<List<ReminderDetails>> =
        firestoreClient.getReminderItems().map { reminderItems ->
            reminderItems.map { reminderItem -> reminderItem.toReminderDetails() }
        }
}

fun FlintApplication.initDependencies() {
    val authClient = AuthClient(Firebase.auth)
    val firestoreClient: DefaultFirestoreClient = FirestoreClient(authClient, Firebase.firestore )

    this.flintRepository = FlintRepository(authClient, firestoreClient)
}
