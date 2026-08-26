package ru.andmar.flint.features.todo.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.andmar.flint.core.data.FlintDao
import ru.andmar.flint.features.todo.domain.model.TodoDetails
import ru.andmar.flint.features.todo.model.toTodoItem

class TodoRepository(private val flintDao: FlintDao) {
    suspend fun createTodo(todoDetails: TodoDetails) = flintDao.insertTodoItem(todoDetails.toTodoItem())

    suspend fun editTodo(todoDetails: TodoDetails) = flintDao.updateTodoItem(todoDetails.toTodoItem())

    suspend fun deleteTodo(todoDetails: TodoDetails) = flintDao.deleteTodoItem(todoDetails.toTodoItem())

    fun getTodoById(todoId: String): Flow<TodoDetails> = flintDao.getTodoItemById(todoId)
        .map { todoItem -> todoItem.toTodoDetails() }

    fun getTodos(): Flow<List<TodoDetails>> = flintDao.getTodoItems().map { todoItems ->
        todoItems.map { todoItem -> todoItem.toTodoDetails() }
    }
}