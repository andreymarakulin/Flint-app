package ru.andmar.flint.features.todo.data.repository

import androidx.compose.ui.text.style.TextDecoration.Companion.combine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.andmar.flint.core.data.FlintDao
import ru.andmar.flint.features.label.data.toLabelDetails
import ru.andmar.flint.features.label.domain.model.LabelDetails
import ru.andmar.flint.features.note.data.toNoteDetails
import ru.andmar.flint.features.reminder.data.toReminderDetails
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails
import ru.andmar.flint.features.todo.data.toTodoDetails
import ru.andmar.flint.features.todo.domain.model.TodoDetails
import ru.andmar.flint.features.todo.data.toTodoItem

class TodoRepository(private val flintDao: FlintDao) {
    suspend fun createTodo(todoDetails: TodoDetails) = flintDao.insertTodoItem(todoDetails.toTodoItem())

    suspend fun editTodo(todoDetails: TodoDetails) = flintDao.updateTodoItem(todoDetails.toTodoItem())

    suspend fun deleteTodo(todoDetails: TodoDetails) = flintDao.deleteTodoItem(todoDetails.toTodoItem())

    suspend fun getTodoByIdOnce(todoId: String): TodoDetails {
        val todoItem = flintDao.getTodoItemById(todoId).first()
        val labelItem = flintDao.getLabelItemById(todoItem.labelId).first()
        val reminderItem = flintDao.getReminderItemById(todoItem.reminderId).first()

        return todoItem.toTodoDetails(
            getLabel = { labelItem.toLabelDetails() },
            getReminder = { reminderItem.toReminderDetails() }
        )
    }

    fun getTodoById(todoId: String): Flow<TodoDetails> {
        return combine(
            flintDao.getTodoItemById(todoId),
            flintDao.getLabelItems(),
            flintDao.getReminderItems()
        ) { todo, labels, reminders ->
            val labelsMap = labels.associateBy { it.id }
            val remindersMap = reminders.associateBy { it.id }

            todo.toTodoDetails(
                getLabel = { labelId -> labelsMap[labelId]?.toLabelDetails() ?: LabelDetails() },
                getReminder = { reminderId -> remindersMap[reminderId]?.toReminderDetails() ?: ReminderDetails() }
            )
        }
    }

    fun getTodos(): Flow<List<TodoDetails>> {
        return combine(
            flintDao.getTodoItems(),
            flintDao.getLabelItems(),
            flintDao.getReminderItems()
        ) { todos, labels, reminders ->
            val labelsMap = labels.associateBy { it.id }
            val remindersMap = reminders.associateBy { it.id }

            todos.map { todoItem ->
                todoItem.toTodoDetails(
                    getLabel = { labelId -> labelsMap[labelId]?.toLabelDetails() ?: LabelDetails() },
                    getReminder = { reminderId -> remindersMap[reminderId]?.toReminderDetails() ?: ReminderDetails() }
                )
            }
        }
    }
}