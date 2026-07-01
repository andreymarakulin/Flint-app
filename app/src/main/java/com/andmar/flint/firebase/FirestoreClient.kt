package com.andmar.flint.firebase

import com.andmar.flint.FlintActions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirestoreClient(
    private val firestore: FirebaseFirestore,
    private val authClient: DefaultAuthClient
): DefaultFirestoreClient {

    override fun setUserItem(
        userItem: UserItem,
        onFlintActions: (FlintActions) -> Unit
    ) {
        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(userItem.uid).set(userItem)
            .addOnCompleteListener { task ->

            }
    }

    override fun addCategoryItem(
        categoryItem: CategoryItem,
        onFlintActions: (FlintActions) -> Unit
    ) {
        onFlintActions(FlintActions.Loading)
        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(authClient.getCurrentUser()!!.uid)
            .collection(CATEGORY_COLLECTION)
            .add(categoryItem)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.set(categoryItem.copy(id = task.result.id))
                    onFlintActions(FlintActions.Success)
                } else onFlintActions(FlintActions.Error(task.exception?.message ?: "Error"))
            }
    }

    override fun setCategoryItem(
        categoryItem: CategoryItem,
        onFlintActions: (FlintActions) -> Unit
    ) {
        onFlintActions(FlintActions.Loading)
        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(authClient.getCurrentUser()!!.uid)
            .collection(CATEGORY_COLLECTION)
            .document(categoryItem.id)
            .set(categoryItem)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onFlintActions(FlintActions.Success)
                } else onFlintActions(FlintActions.Error(task.exception?.message ?: "Error"))
            }
    }

    override fun deleteCategoryItem(
        categoryItem: CategoryItem,
        onFlintActions: (FlintActions) -> Unit
    ) {
        onFlintActions(FlintActions.Loading)
        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(authClient.getCurrentUser()!!.uid)
            .collection(CATEGORY_COLLECTION)
            .document(categoryItem.id)
            .delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onFlintActions(FlintActions.Success)
                } else onFlintActions(FlintActions.Error(task.exception?.message ?: "Error"))
            }
    }

    override fun getCategoryItem(categoryId: String): Flow<CategoryItem> = callbackFlow {
        val snapshot = firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(authClient.getCurrentUser()!!.uid)
            .collection(CATEGORY_COLLECTION)
            .document(categoryId)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    trySend(snapshot.toObject(CategoryItem::class.java) as CategoryItem)
                } else CategoryItem()
            }
        awaitClose { snapshot.remove() }
    }

    override fun getCategoryItems(): Flow<List<CategoryItem>> = callbackFlow {
        val uid = authClient.getCurrentUser()?.uid
        if (uid == null) {
            trySend(emptyList())
            channel.close()
            return@callbackFlow
        }

        val snapshot = firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(uid)
            .collection(CATEGORY_COLLECTION)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    trySend(snapshot.toObjects(CategoryItem::class.java))
                } else trySend(emptyList())
            }
        awaitClose { snapshot.remove() }
    }

    override fun addNoteItem(
        noteItem: NoteItem,
        onFlintActions: (FlintActions) -> Unit
    ) {
        onFlintActions(FlintActions.Loading)
        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(authClient.getCurrentUser()!!.uid)
            .collection(NOTE_COLLECTION).add(noteItem)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.set(noteItem.copy(id = task.result.id))
                    onFlintActions(FlintActions.Success)
                } else onFlintActions(FlintActions.Error(task.exception?.message.toString()))
            }
    }

    override fun setNoteItem(
        noteItem: NoteItem,
        onFlintActions: (FlintActions) -> Unit
    ) {
        onFlintActions(FlintActions.Loading)
        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(authClient.getCurrentUser()!!.uid)
            .collection(NOTE_COLLECTION)
            .document(noteItem.id).set(noteItem)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onFlintActions(FlintActions.Success)
                } else onFlintActions(FlintActions.Error(task.exception?.message.toString()))
            }
    }

    override fun deleteNoteItem(
        noteItem: NoteItem,
        onFlintActions: (FlintActions) -> Unit
    ) {
        onFlintActions(FlintActions.Loading)
        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(authClient.getCurrentUser()!!.uid)
            .collection(NOTE_COLLECTION)
            .document(noteItem.id).delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onFlintActions(FlintActions.Success)
                } else onFlintActions(FlintActions.Error(task.exception?.message.toString()))
            }
    }

    override fun getNoteItem(noteId: String): Flow<NoteItem> = callbackFlow {
       val snapshot = firestore.collection(APPS_COLLECTION)
           .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(authClient.getCurrentUser()!!.uid)
            .collection(NOTE_COLLECTION)
            .document(noteId)
           .addSnapshotListener { snapshot, exception ->
               if (snapshot != null) {
                   trySend(snapshot.toObject(NoteItem::class.java) as NoteItem)
               } else trySend(NoteItem())
           }
        awaitClose { snapshot.remove() }
    }

    override fun getNoteItems(): Flow<List<NoteItem>> = callbackFlow {
        val uid = authClient.getCurrentUser()?.uid
        if (uid == null) {
            trySend(emptyList())
            channel.close()
            return@callbackFlow
        }
        val snapshot = firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(uid)
            .collection(NOTE_COLLECTION)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    trySend(snapshot.toObjects(NoteItem::class.java))
                } else trySend(emptyList())
            }
        awaitClose { snapshot.remove() }
    }

    override fun addTodoItem(
        todoItem: TodoItem,
        onFlintActions: (FlintActions) -> Unit
    ) {
        onFlintActions(FlintActions.Loading)
        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(authClient.getCurrentUser()!!.uid)
            .collection(TODO_COLLECTION).add(todoItem)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.set(todoItem.copy(id = task.result.id))
                    onFlintActions(FlintActions.Success)
                } else onFlintActions(FlintActions.Error(task.exception?.message.toString()))
            }
    }

    override fun setTodoItem(
        todoItem: TodoItem,
        onFlintActions: (FlintActions) -> Unit
    ) {
        onFlintActions(FlintActions.Loading)
        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(authClient.getCurrentUser()!!.uid)
            .collection(TODO_COLLECTION)
            .document(todoItem.id)
            .set(todoItem)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onFlintActions(FlintActions.Success)
                } else onFlintActions(FlintActions.Error(task.exception?.message.toString()))
            }
    }

    override fun deleteTodoItem(
        todoItem: TodoItem,
        onFlintActions: (FlintActions) -> Unit
    ) {
        onFlintActions(FlintActions.Loading)
        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(authClient.getCurrentUser()!!.uid)
            .collection(TODO_COLLECTION)
            .document(todoItem.id)
            .delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onFlintActions(FlintActions.Success)
                } else onFlintActions(FlintActions.Error(task.exception?.message.toString()))
            }
    }

    override fun getTodoItem(todoId: String): Flow<TodoItem> = callbackFlow {
        val snapshot = firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(authClient.getCurrentUser()!!.uid)
            .collection(TODO_COLLECTION)
            .document(todoId)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    trySend(snapshot.toObject(TodoItem::class.java) as TodoItem)
                } else trySend(TodoItem())
            }
        awaitClose { snapshot.remove() }
    }

    override fun getTodoItems(): Flow<List<TodoItem>> = callbackFlow {
        val snapshot = firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(authClient.getCurrentUser()!!.uid)
            .collection(TODO_COLLECTION)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    trySend(snapshot.toObjects(TodoItem::class.java))
                } else trySend(emptyList())
            }
        awaitClose { snapshot.remove() }
    }

    override fun addLabelItem(
        labelItem: LabelItem,
        onFlintActions: (FlintActions) -> Unit
    ) {
        onFlintActions(FlintActions.Loading)
        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(authClient.getCurrentUser()!!.uid)
            .collection(LABEL_COLLECTION).add(labelItem)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.set(labelItem.copy(id = task.result.id))
                    onFlintActions(FlintActions.Success)
                } else onFlintActions(FlintActions.Error(task.exception?.message.toString()))
            }
    }

    override fun setLabelItem(
        labelItem: LabelItem,
        onFlintActions: (FlintActions) -> Unit
    ) {
        onFlintActions(FlintActions.Loading)
        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(authClient.getCurrentUser()!!.uid)
            .collection(LABEL_COLLECTION)
            .document(labelItem.id)
            .set(labelItem)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onFlintActions(FlintActions.Success)
                } else onFlintActions(FlintActions.Error(task.exception?.message.toString()))
            }
    }

    override fun getLabelItem(labelId: String): Flow<LabelItem> = callbackFlow {
        val snapshot = firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(authClient.getCurrentUser()!!.uid)
            .collection(LABEL_COLLECTION)
            .document(labelId)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    trySend(snapshot.toObject(LabelItem::class.java) as LabelItem)
                } else trySend(LabelItem())
            }
        awaitClose { snapshot.remove() }
    }

    override fun getLabelItems(): Flow<List<LabelItem>> = callbackFlow {
        val snapshot = firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(authClient.getCurrentUser()!!.uid)
            .collection(LABEL_COLLECTION)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    trySend(snapshot.toObjects(LabelItem::class.java))
                } else trySend(emptyList())
            }
        awaitClose { snapshot.remove() }
    }

    override fun addReminderItem(
        reminderItem: ReminderItem,
        onFlintActions: (FlintActions) -> Unit
    ) {
        onFlintActions(FlintActions.Loading)
        firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(authClient.getCurrentUser()!!.uid)
            .collection(REMINDER_COLLECTION).add(reminderItem)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.set(reminderItem.copy(id = task.result.id))
                    onFlintActions(FlintActions.Success)
                } else onFlintActions(FlintActions.Error(task.exception?.message.toString()))
            }
    }

    override fun setReminderItem(
        reminderItem: ReminderItem,
        onFlintActions: (FlintActions) -> Unit
    ) {
        onFlintActions(FlintActions.Loading)
        firestore.collection(APPS_COLLECTION).document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(authClient.getCurrentUser()!!.uid)
            .collection(REMINDER_COLLECTION)
            .document(reminderItem.id)
            .set(reminderItem)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onFlintActions(FlintActions.Success)
                } else onFlintActions(FlintActions.Error(task.exception?.message.toString()))
            }
    }

    override fun getReminderItem(reminderId: String): Flow<ReminderItem> = callbackFlow {
        val snapshot = firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(authClient.getCurrentUser()!!.uid)
            .collection(REMINDER_COLLECTION)
            .document(reminderId)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    trySend(snapshot.toObject(ReminderItem::class.java) as ReminderItem)
                } else trySend(ReminderItem())
            }
        awaitClose { snapshot.remove() }
    }

    override fun getReminderItems(): Flow<List<ReminderItem>> = callbackFlow {
        val snapshot = firestore.collection(APPS_COLLECTION)
            .document(APP_DOCUMENT)
            .collection(USER_COLLECTION)
            .document(authClient.getCurrentUser()!!.uid)
            .collection(REMINDER_COLLECTION)
            .addSnapshotListener { snapshot, exception ->
                if (snapshot != null) {
                    trySend(snapshot.toObjects(ReminderItem::class.java))
                } else trySend(emptyList())
            }
        awaitClose { snapshot.remove() }
    }
}