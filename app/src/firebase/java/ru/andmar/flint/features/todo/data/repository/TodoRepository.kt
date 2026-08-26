package ru.andmar.flint.features.todo.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.andmar.flint.core.data.DefaultFirestoreClient
import ru.andmar.flint.features.todo.data.toTodoDetails
import ru.andmar.flint.features.todo.data.toTodoItem
import ru.andmar.flint.features.todo.domain.model.TodoDetails

class TodoRepository(private val firestoreClient: DefaultFirestoreClient) {

    suspend fun createTodo(todoDetails: TodoDetails) = firestoreClient.setTodoItem(todoDetails.toTodoItem())
    suspend fun editTodo(todoDetails: TodoDetails) = firestoreClient.setTodoItem(todoDetails.toTodoItem())
    suspend fun deleteTodo(todoDetails: TodoDetails) = firestoreClient.deleteTodoItem(todoDetails.id)
    suspend fun getTodoByIdOnce(todoId: String): TodoDetails = firestoreClient.getTodoItemOnce(todoId).toTodoDetails()
    fun getTodoById(todoId: String): Flow<TodoDetails> =
        firestoreClient.getTodoItem(todoId).map { todoItem -> todoItem.toTodoDetails() }
    fun getTodos(): Flow<List<TodoDetails>> =
        firestoreClient.getTodoItems().map { todoItems ->
            todoItems.map { todoItem -> todoItem.toTodoDetails() }
        }
}