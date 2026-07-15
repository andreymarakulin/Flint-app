package com.andmar.flint

import com.andmar.flint.room.AuthItem
import com.andmar.flint.room.CategoryItem
import com.andmar.flint.room.FlintDao
import com.andmar.flint.room.FlintDatabase
import com.andmar.flint.room.LabelItem
import com.andmar.flint.room.NoteItem
import com.andmar.flint.room.ReminderItem
import com.andmar.flint.room.TodoItem
import com.andmar.flint.room.UserItem
import com.andmar.flint.room.toCategoryDetails
import com.andmar.flint.room.toCategoryItem
import com.andmar.flint.room.toCreateCategoryItem
import com.andmar.flint.room.toCreateNoteItem
import com.andmar.flint.room.toCreateTodoItem
import com.andmar.flint.room.toNoteDetails
import com.andmar.flint.room.toNoteItem
import com.andmar.flint.room.toTodoDetails
import com.andmar.flint.room.toTodoItem
import com.andmar.flint.ui.theme.account.AuthDetails
import com.andmar.flint.ui.theme.category.CategoryDetails
import com.andmar.flint.ui.theme.note.NoteDetails
import com.andmar.flint.ui.theme.todo.TodoDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class FlintRepository(
    private val flintDao: FlintDao
) {

    //Auth
    fun getAuthState(): Flow<FlintActions> = flowOf(FlintActions.Success)
    suspend fun signIn(authDetails: AuthDetails) {
        TODO("Not yet implemented")
    }

    suspend fun createUser(authDetails: AuthDetails) {
        TODO("Not yet implemented")
    }

    suspend fun signOut() {
        TODO("Not yet implemented")
    }

    suspend fun createUser(userItem: UserItem) {
        TODO("Not yet implemented")
    }

    //Category
    suspend fun createCategory(categoryDetails: CategoryDetails) = flintDao.insertCategoryItem(categoryDetails.toCreateCategoryItem())

    suspend fun editCategory(categoryDetails: CategoryDetails) = flintDao.updateCategoryItem(categoryDetails.toCategoryItem())

    suspend fun deleteCategory(categoryDetails: CategoryDetails) = flintDao.deleteCategoryItem(categoryDetails.toCategoryItem())

    fun getCategoryById(categoryId: String): Flow<CategoryDetails> = flintDao.getCategoryItemById(categoryId)
        .map { categoryItem -> categoryItem.toCategoryDetails() }

    fun getCategories(): Flow<List<CategoryDetails>> = flintDao.getCategoryItems()
        .map { categoryItems ->
            categoryItems.map { categoryItem -> categoryItem.toCategoryDetails() }
        }

    //Note
    suspend fun createNote(noteDetails: NoteDetails) = flintDao.insertNoteItem(noteDetails.toCreateNoteItem())

    suspend fun editNote(noteDetails: NoteDetails) = flintDao.updateNoteItem(noteDetails.toNoteItem())

    suspend fun deleteNote(noteDetails: NoteDetails) = flintDao.deleteNoteItem(noteDetails.toNoteItem())

    fun getNoteById(noteId: String): Flow<NoteDetails> = flintDao.getNoteItemById(noteId)
        .map { noteItem -> noteItem.toNoteDetails() }

    fun getNotes(): Flow<List<NoteDetails>> = flintDao.getNoteItems().map { noteItems ->
        noteItems.map { noteItem -> noteItem.toNoteDetails() }
    }

    //Todo
    suspend fun createTodo(todoDetails: TodoDetails) = flintDao.insertTodoItem(todoDetails.toCreateTodoItem())

    suspend fun editTodo(todoDetails: TodoDetails) = flintDao.updateTodoItem(todoDetails.toTodoItem())

    suspend fun deleteTodo(todoDetails: TodoDetails) = flintDao.deleteTodoItem(todoDetails.toTodoItem())

    fun getTodoById(todoId: String): Flow<TodoDetails> = flintDao.getTodoItemById(todoId)
        .map { todoItem -> todoItem.toTodoDetails() }

    fun getTodos(): Flow<List<TodoDetails>> = flintDao.getTodoItems().map { todoItems ->
        todoItems.map { todoItem -> todoItem.toTodoDetails() }
    }

    //Label
    fun createLabel(labelItem: LabelItem) {
        TODO("Not yet implemented")
    }

    fun editLabel(labelItem: LabelItem) {
        TODO("Not yet implemented")
    }

    fun getLabels(): Flow<List<LabelItem>> {
        TODO("Not yet implemented")
    }

    //Reminder
    fun createReminder(reminderItem: ReminderItem) {
        TODO("Not yet implemented")
    }

    fun editReminder(reminderItem: ReminderItem) {
        TODO("Not yet implemented")
    }

    fun getReminders(): Flow<List<ReminderItem>> {
        TODO("Not yet implemented")
    }
}

fun FlintApplication.initDependencies() {
    this.flintRepository = FlintRepository(FlintDatabase.getDatabase(applicationContext).flintDao())
}
