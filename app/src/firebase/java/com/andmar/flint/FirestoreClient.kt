package com.andmar.flint

import android.system.Os.close
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreClient(
    private val authClient: DefaultAuthClient,
    private val firestore: FirebaseFirestore
): DefaultFirestoreClient {

    override suspend fun setUserItem(userItem: UserItem) {
        val uid = authClient.getCurrentUser()!!.uid
        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(uid)
            .set(userItem.copy(uid = uid))
            .await()
    }

    override suspend fun addCategoryItem(categoryItem: CategoryItem) {
        val uid = authClient.getCurrentUser()!!.uid

        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(uid)
            .collection(CATEGORY_COLLECTION)
            .add(categoryItem)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.set(categoryItem.copy(id = task.result.id))
                }
            }.await()
    }

    override suspend fun setCategoryItem(categoryItem: CategoryItem) {
        val uid = authClient.getCurrentUser()!!.uid

        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(uid)
            .collection(CATEGORY_COLLECTION)
            .document(categoryItem.id)
            .set(categoryItem)
            .await()
    }

    override suspend fun deleteCategoryItem(categoryId: String) {
        val uid = authClient.getCurrentUser()!!.uid

        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(uid)
            .collection(CATEGORY_COLLECTION)
            .document(categoryId)
            .delete()
            .await()
    }

    override fun getCategoryItem(categoryId: String): Flow<CategoryItem> = callbackFlow {
        val uid = authClient.getCurrentUser()?.uid

        if (uid == null) {
            close()
            return@callbackFlow
        }

        val request = firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
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
        val uid = authClient.getCurrentUser()?.uid

        if (uid == null) {
            close()
            return@callbackFlow
        }

        val request = firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
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

    override suspend fun addNoteItem(noteItem: NoteItem) {
        val uid = authClient.getCurrentUser()!!.uid

        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(uid)
            .collection(NOTE_COLLECTION)
            .add(noteItem)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.set(noteItem.copy(id = task.result.id))
                }
            }.await()
    }

    override suspend fun setNoteItem(noteItem: NoteItem) {
        val uid = authClient.getCurrentUser()!!.uid

        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(NOTE_COLLECTION)
            .document(noteItem.id)
            .set(noteItem)
            .await()
    }

    override suspend fun deleteNoteItem(noteId: String) {
        val uid = authClient.getCurrentUser()!!.uid

        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(uid)
            .collection(NOTE_COLLECTION)
            .document(noteId)
            .delete()
            .await()
    }

    override fun getNoteItem(noteId: String): Flow<NoteItem> = callbackFlow {
        val uid = authClient.getCurrentUser()?.uid

        if (uid == null) {
            close()
            return@callbackFlow
        }

        val request = firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
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
        val uid = authClient.getCurrentUser()?.uid

        if (uid == null) {
            close()
            return@callbackFlow
        }
        val request = firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
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

    override suspend fun addTodoItem(todoItem: TodoItem) {
        val uid = authClient.getCurrentUser()!!.uid

        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(uid)
            .collection(TODO_COLLECTION)
            .add(todoItem)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.set(todoItem.copy(id = task.result.id))
                }
            }.await()
    }

    override suspend fun setTodoItem(todoItem: TodoItem) {
        val uid = authClient.getCurrentUser()!!.uid

        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(uid)
            .collection(TODO_COLLECTION)
            .document(todoItem.id)
            .set(todoItem)
            .await()
    }

    override suspend fun deleteTodoItem(todoId: String) {
        val uid = authClient.getCurrentUser()!!.uid

        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(uid)
            .collection(TODO_COLLECTION)
            .document(todoId)
            .delete()
            .await()
    }

    override fun getTodoItem(todoId: String): Flow<TodoItem> = callbackFlow {
        val uid = authClient.getCurrentUser()?.uid

        if (uid == null) {
            close()
            return@callbackFlow
        }

        val request = firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
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
        val uid = authClient.getCurrentUser()?.uid

        if (uid == null) {
            close()
            return@callbackFlow
        }

        val request = firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
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

    override suspend fun addLabelItem(labelItem: LabelItem) {
        val uid = authClient.getCurrentUser()!!.uid

        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(uid)
            .collection(LABEL_COLLECTION)
            .add(labelItem)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.set(labelItem.copy(id = task.result.id))
                }
            }.await()
    }

    override suspend fun setLabelItem(labelItem: LabelItem) {
        val uid = authClient.getCurrentUser()!!.uid

        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(uid)
            .collection(LABEL_COLLECTION)
            .document(labelItem.id)
            .set(labelItem)
            .await()
    }

    override suspend fun deleteLabelItem(labelId: String) {
        val uid = authClient.getCurrentUser()!!.uid

        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(uid)
            .collection(LABEL_COLLECTION)
            .document(labelId)
            .delete()
            .await()
    }

    override fun getLabelItem(labelId: String): Flow<LabelItem> = callbackFlow {
        val uid = authClient.getCurrentUser()?.uid

        if (uid == null) {
            close()
            return@callbackFlow
        }

        val request = firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
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
        val uid = authClient.getCurrentUser()?.uid

        if (uid == null) {
            close()
            return@callbackFlow
        }

        val request = firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
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

    override suspend fun addReminderItem(reminderItem: ReminderItem) {
        val uid = authClient.getCurrentUser()!!.uid

        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(uid)
            .collection(REMINDER_COLLECTION)
            .add(reminderItem)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.set(reminderItem.copy(id = task.result.id))
                }
            }.await()
    }

    override suspend fun setReminderItem(reminderItem: ReminderItem) {
        val uid = authClient.getCurrentUser()!!.uid

        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(uid)
            .collection(REMINDER_COLLECTION)
            .document(reminderItem.id)
            .set(reminderItem)
            .await()
    }

    override suspend fun deleteReminderItem(reminderId: String) {
        val uid = authClient.getCurrentUser()!!.uid

        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(uid)
            .collection(REMINDER_COLLECTION)
            .document(reminderId)
            .delete()
            .await()
    }

    override fun getReminderItem(reminderId: String): Flow<ReminderItem> = callbackFlow {
        val uid = authClient.getCurrentUser()?.uid

        if (uid == null) {
            close()
            return@callbackFlow
        }

        val request = firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
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
        val uid = authClient.getCurrentUser()?.uid

        if (uid == null) {
            close()
            return@callbackFlow
        }

        val request = firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
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