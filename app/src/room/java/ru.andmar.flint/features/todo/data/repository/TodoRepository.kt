package ru.andmar.flint.features.todo.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.andmar.flint.core.data.FlintDao
import ru.andmar.flint.features.todo.data.toTodoDetails
import ru.andmar.flint.features.todo.domain.model.TodoDetails
import ru.andmar.flint.features.todo.data.toTodoItem

class TodoRepository(private val flintDao: FlintDao) {
    suspend fun createTodo(todoDetails: TodoDetails) = flintDao.insertTodoItem(todoDetails.toTodoItem())

    suspend fun editTodo(todoDetails: TodoDetails) = flintDao.updateTodoItem(todoDetails.toTodoItem())

    suspend fun deleteTodo(todoDetails: TodoDetails) = flintDao.deleteTodoItem(todoDetails.toTodoItem())

    suspend fun getTodoByIdOnce(todoId: String): TodoDetails = flintDao.getTodoItemById(todoId)
        .map { todoItem -> todoItem.toTodoDetails() }.first()

    fun getTodoById(todoId: String): Flow<TodoDetails> = flintDao.getTodoItemById(todoId)
        .map { todoItem -> todoItem.toTodoDetails() }

    fun getTodos(): Flow<List<TodoDetails>> = flintDao.getTodoItems().map { todoItems ->
        todoItems.map { todoItem -> todoItem.toTodoDetails() }
    }
}