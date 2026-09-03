package ru.andmar.flint.core.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import ru.andmar.flint.features.account.data.model.UserItem
import ru.andmar.flint.features.category.data.model.CategoryItem
import ru.andmar.flint.features.label.data.model.LabelItem
import ru.andmar.flint.features.note.data.model.NoteItem
import ru.andmar.flint.features.reminder.data.model.ReminderItem
import ru.andmar.flint.features.todo.data.model.TodoItem


const val USER_COLLECTION = "Users"
const val CATEGORY_COLLECTION = "Categories"
const val NOTE_COLLECTION = "Notes"
const val TODO_COLLECTION = "Todos"
const val LABEL_COLLECTION = "Labels"
const val REMINDER_COLLECTION = "Reminder"



class FirestoreClient(
    private val authClient: DefaultAuthClient,
    private val firestore: FirebaseFirestore
): DefaultFirestoreClient {

    override suspend fun setUserItem() {
        val uid = authClient.getCurrentUser()?.uid ?: throw IllegalStateException("User not auth")
        val email = authClient.getCurrentUser()?.email

        firestore.collection(USER_COLLECTION)
            .document(uid)
            .set(
                UserItem(
                    uid = uid,
                    email = email.toString()
                )
            ).await()
    }

    override suspend fun getUserItem(): UserItem {
        val uid = authClient.getCurrentUser()?.uid ?: throw IllegalStateException("User not auth")

        Log.i("uid", uid)

        val snapshot = firestore.collection(USER_COLLECTION)
            .document(uid)
            .get()
            .await()

        if (!snapshot.exists()) {
            throw NoSuchElementException("Документ не найден")
        }

        return snapshot.toObject(UserItem::class.java)
            ?: throw IllegalArgumentException("Ошибка маппинга полей")
    }

    override suspend fun setCategoryItem(categoryItem: CategoryItem) {
        val uid = authClient.getCurrentUser()?.uid ?: throw IllegalStateException("User not auth")

        firestore.collection(USER_COLLECTION)
            .document(uid)
            .collection(CATEGORY_COLLECTION)
            .document(categoryItem.id)
            .set(categoryItem)
            .await()
    }

    override suspend fun deleteCategoryItem(categoryId: String) {
        val uid = authClient.getCurrentUser()?.uid ?: throw IllegalStateException("User not auth")

        firestore.collection(USER_COLLECTION)
            .document(uid)
            .collection(CATEGORY_COLLECTION)
            .document(categoryId)
            .delete()
            .await()
    }

    override suspend fun getCategoryItemOnce(categoryId: String): CategoryItem {
        val uid = authClient.getCurrentUser()?.uid ?: throw IllegalStateException("User not auth")

        val snapshot = firestore.collection(USER_COLLECTION)
            .document(uid)
            .collection(CATEGORY_COLLECTION)
            .document(categoryId)
            .get()
            .await()

        if (!snapshot.exists()) {
            throw NoSuchElementException("Документ не найден")
        }

        return snapshot.toObject(CategoryItem::class.java)
            ?: throw IllegalArgumentException("Ошибка маппинга полей")
    }

    override fun getCategoryItem(categoryId: String): Flow<CategoryItem> = callbackFlow {
        val uid = authClient.getCurrentUser()?.uid ?: throw IllegalStateException("User not auth")

        val request = firestore.collection(USER_COLLECTION)
            .document(uid)
            .collection(CATEGORY_COLLECTION)
            .document(categoryId)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    try {
                        val item = snapshot.toObject(CategoryItem::class.java) ?: CategoryItem()
                        trySend(item)
                    } catch (e: Exception) {
                        close(e)
                    }
                }
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }
            }
        awaitClose { request.remove() }
    }

    override fun getCategoryItems(): Flow<List<CategoryItem>> = callbackFlow {
        val uid = authClient.getCurrentUser()?.uid ?: throw IllegalStateException("User not auth")

        val request = firestore.collection(USER_COLLECTION)
            .document(uid)
            .collection(CATEGORY_COLLECTION)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    try {
                        val item = snapshot.toObjects(CategoryItem::class.java)
                        trySend(item)
                    } catch (e: Exception) {
                        close(e)
                    }
                }
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }
            }
        awaitClose { request.remove() }
    }

    override suspend fun setNoteItem(noteItem: NoteItem) {
        val uid = authClient.getCurrentUser()?.uid ?: throw IllegalStateException("User not auth")

        firestore.collection(USER_COLLECTION)
            .document(uid)
            .collection(NOTE_COLLECTION)
            .document(noteItem.id)
            .set(noteItem)
            .await()
    }

    override suspend fun deleteNoteItem(noteId: String) {
        val uid = authClient.getCurrentUser()?.uid ?: throw IllegalStateException("User not auth")

        firestore.collection(USER_COLLECTION)
            .document(uid)
            .collection(NOTE_COLLECTION)
            .document(noteId)
            .delete()
            .await()
    }

    override suspend fun getNoteItemOnce(noteId: String): NoteItem {
        val uid = authClient.getCurrentUser()?.uid ?: throw IllegalStateException("User not auth")

        val snapshot = firestore.collection(USER_COLLECTION)
            .document(uid)
            .collection(NOTE_COLLECTION)
            .document(noteId)
            .get()
            .await()

        if (!snapshot.exists()) {
            throw NoSuchElementException("Документ не найден")
        }

        return snapshot.toObject(NoteItem::class.java)
            ?: throw IllegalArgumentException("Ошибка маппинга полей")
    }

    override fun getNoteItem(noteId: String): Flow<NoteItem> = callbackFlow {
        val uid = authClient.getCurrentUser()?.uid ?: throw IllegalStateException("User not auth")

        val request = firestore.collection(USER_COLLECTION)
            .document(uid)
            .collection(NOTE_COLLECTION)
            .document(noteId)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    try {
                        val item = snapshot.toObject(NoteItem::class.java) ?: NoteItem()
                        trySend(item)
                    } catch (e: Exception) {
                        close(e)
                    }
                }
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }
            }
        awaitClose { request.remove() }
    }

    override fun getNoteItems(): Flow<List<NoteItem>> = callbackFlow {
        val uid = authClient.getCurrentUser()?.uid ?: throw IllegalStateException("User not auth")

        val request = firestore.collection(USER_COLLECTION)
            .document(uid)
            .collection(NOTE_COLLECTION)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    try {
                        val item = snapshot.toObjects(NoteItem::class.java)
                        trySend(item)
                    } catch (e: Exception) {
                        close(e)
                    }
                }
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }
            }
        awaitClose { request.remove() }
    }

    override suspend fun setTodoItem(todoItem: TodoItem) {
        val uid = authClient.getCurrentUser()?.uid ?: throw IllegalStateException("User not auth")

        firestore.collection(USER_COLLECTION)
            .document(uid)
            .collection(TODO_COLLECTION)
            .document(todoItem.id)
            .set(todoItem)
            .await()
    }

    override suspend fun deleteTodoItem(todoId: String) {
        val uid = authClient.getCurrentUser()?.uid ?: throw IllegalStateException("User not auth")

        firestore.collection(USER_COLLECTION)
            .document(uid)
            .collection(TODO_COLLECTION)
            .document(todoId)
            .delete()
            .await()
    }

    override suspend fun getTodoItemOnce(todoId: String): TodoItem {
        val uid = authClient.getCurrentUser()?.uid ?: throw IllegalStateException("User not auth")

        val snapshot = firestore.collection(USER_COLLECTION)
            .document(uid)
            .collection(TODO_COLLECTION)
            .document(todoId)
            .get()
            .await()

        if (!snapshot.exists()) {
            throw NoSuchElementException("Документ не найден")
        }

        return snapshot.toObject(TodoItem::class.java)
            ?: throw IllegalArgumentException("Ошибка маппинга полей")
    }


    override fun getTodoItem(todoId: String): Flow<TodoItem> = callbackFlow {
        val uid = authClient.getCurrentUser()?.uid ?: throw IllegalStateException("User not auth")

        val request = firestore.collection(USER_COLLECTION)
            .document(uid)
            .collection(TODO_COLLECTION)
            .document(todoId)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    try {
                        val item = snapshot.toObject(TodoItem::class.java) ?: TodoItem()
                        trySend(item)
                    } catch (e: Exception) {
                        close(e)
                    }
                }
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }
            }
        awaitClose { request.remove() }
    }

    override fun getTodoItems(): Flow<List<TodoItem>> = callbackFlow {
        val uid = authClient.getCurrentUser()?.uid ?: throw IllegalStateException("User not auth")

        val request = firestore.collection(USER_COLLECTION)
            .document(uid)
            .collection(TODO_COLLECTION)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    try {
                        val item = snapshot.toObjects(TodoItem::class.java)
                        trySend(item)
                    } catch (e: Exception) {
                        close(e)
                    }
                }
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }
            }
        awaitClose { request.remove() }
    }

    override suspend fun setLabelItem(labelItem: LabelItem) {
        val uid = authClient.getCurrentUser()?.uid ?: throw IllegalStateException("User not auth")

        firestore.collection(USER_COLLECTION)
            .document(uid)
            .collection(LABEL_COLLECTION)
            .document(labelItem.id)
            .set(labelItem)
            .await()
    }

    override suspend fun deleteLabelItem(labelId: String) {
        val uid = authClient.getCurrentUser()?.uid ?: throw IllegalStateException("User not auth")

        firestore.collection(USER_COLLECTION)
            .document(uid)
            .collection(LABEL_COLLECTION)
            .document(labelId)
            .delete()
            .await()
    }

    override suspend fun getLabelItemOnce(labelId: String): LabelItem {
        val uid = authClient.getCurrentUser()?.uid ?: throw IllegalStateException("User not auth")

        val snapshot = firestore.collection(USER_COLLECTION)
            .document(uid)
            .collection(LABEL_COLLECTION)
            .document(labelId)
            .get()
            .await()

        if (!snapshot.exists()) {
            throw NoSuchElementException("Документ не найден")
        }

        return snapshot.toObject(LabelItem::class.java)
            ?: throw IllegalArgumentException("Ошибка маппинга полей")
    }

    override fun getLabelItem(labelId: String): Flow<LabelItem> = callbackFlow {
        val uid = authClient.getCurrentUser()?.uid ?: throw IllegalStateException("User not auth")

        val request = firestore.collection(USER_COLLECTION)
            .document(uid)
            .collection(LABEL_COLLECTION)
            .document(labelId)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    try {
                        val item = snapshot.toObject(LabelItem::class.java) ?: LabelItem()
                        trySend(item)
                    } catch (e: Exception) {
                        close(e)
                    }
                }
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }
            }
        awaitClose { request.remove() }
    }

    override fun getLabelItems(): Flow<List<LabelItem>> = callbackFlow {
        val uid = authClient.getCurrentUser()?.uid ?: throw IllegalStateException("User not auth")

        val request = firestore.collection(USER_COLLECTION)
            .document(uid)
            .collection(LABEL_COLLECTION)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    try {
                        val item = snapshot.toObjects(LabelItem::class.java)
                        trySend(item)
                    } catch (e: Exception) {
                        close(e)
                    }
                }
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }
            }
        awaitClose { request.remove() }
    }

    override suspend fun setReminderItem(reminderItem: ReminderItem) {
        val uid = authClient.getCurrentUser()?.uid ?: throw IllegalStateException("User not auth")

        firestore.collection(USER_COLLECTION)
            .document(uid)
            .collection(REMINDER_COLLECTION)
            .document(reminderItem.id)
            .set(reminderItem)
            .await()
    }

    override suspend fun deleteReminderItem(reminderId: String) {
        val uid = authClient.getCurrentUser()?.uid ?: throw IllegalStateException("User not auth")

        firestore.collection(USER_COLLECTION)
            .document(uid)
            .collection(REMINDER_COLLECTION)
            .document(reminderId)
            .delete()
            .await()
    }

    override suspend fun updateReminderDoneState(reminderId: String) {
        val uid = authClient.getCurrentUser()?.uid ?: throw IllegalStateException("User not auth")

        firestore.collection(USER_COLLECTION)
            .document(uid)
            .collection(REMINDER_COLLECTION)
            .document(reminderId)
            .update("done", true)
    }

    override suspend fun getReminderItemOnce(reminderId: String): ReminderItem {
        val uid = authClient.getCurrentUser()?.uid ?: throw IllegalStateException("User not auth")

        val snapshot = firestore.collection(USER_COLLECTION)
            .document(uid)
            .collection(REMINDER_COLLECTION)
            .document(reminderId)
            .get()
            .await()

        if (!snapshot.exists()) {
            throw NoSuchElementException("Документ не найден")
        }

        return snapshot.toObject(ReminderItem::class.java)
            ?: throw IllegalArgumentException("Ошибка маппинга полей")
    }

    override fun getReminderItem(reminderId: String): Flow<ReminderItem> = callbackFlow {
        val uid = authClient.getCurrentUser()?.uid ?: throw IllegalStateException("User not auth")

        val request = firestore.collection(USER_COLLECTION)
            .document(uid)
            .collection(REMINDER_COLLECTION)
            .document(reminderId)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    try {
                        val item = snapshot.toObject(ReminderItem::class.java) ?: ReminderItem()
                        trySend(item)
                    } catch (e: Exception) {
                        close(e)
                    }
                }
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }
            }
        awaitClose { request.remove() }
    }

    override fun getReminderItems(): Flow<List<ReminderItem>> = callbackFlow {
        val uid = authClient.getCurrentUser()?.uid ?: throw IllegalStateException("User not auth")

        val request = firestore.collection(USER_COLLECTION)
            .document(uid)
            .collection(REMINDER_COLLECTION)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    try {
                        val item = snapshot.toObjects(ReminderItem::class.java)
                        trySend(item)
                    } catch (e: Exception) {
                        close(e)
                    }
                }
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }
            }
        awaitClose { request.remove() }
    }
}