package ru.andmar.flint.features.todo.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import ru.andmar.flint.core.data.DefaultFirestoreClient
import ru.andmar.flint.features.label.data.model.LabelItem
import ru.andmar.flint.features.label.data.toLabelDetails
import ru.andmar.flint.features.label.domain.model.LabelDetails
import ru.andmar.flint.features.reminder.data.model.ReminderItem
import ru.andmar.flint.features.reminder.data.toReminderDetails
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails
import ru.andmar.flint.features.todo.data.toTodoDetails
import ru.andmar.flint.features.todo.data.toTodoItem
import ru.andmar.flint.features.todo.domain.model.TodoDetails

class TodoRepository(private val firestoreClient: DefaultFirestoreClient) {

    suspend fun createTodo(todoDetails: TodoDetails) = firestoreClient.setTodoItem(todoDetails.toTodoItem())
    suspend fun editTodo(todoDetails: TodoDetails) = firestoreClient.setTodoItem(todoDetails.toTodoItem())
    suspend fun deleteTodo(todoDetails: TodoDetails) = firestoreClient.deleteTodoItem(todoDetails.id)
    suspend fun getTodoByIdOnce(todoId: String): TodoDetails {
        val todoItem = firestoreClient.getTodoItemOnce(todoId)
        val labelItem: LabelItem? = if (todoItem.labelId.isNotBlank()) {
            firestoreClient.getLabelItemOnce(todoItem.labelId)
        } else null
        val reminderItem: ReminderItem? = if (todoItem.reminderId.isNotBlank()) {
            firestoreClient.getReminderItemOnce(todoItem.reminderId)
        } else null

        return todoItem.toTodoDetails(
            getLabel = { labelItem?.toLabelDetails() ?: LabelDetails() },
            getReminder = { reminderItem?.toReminderDetails() ?: ReminderDetails() }
        )
    }

    fun getTodoById(todoId: String): Flow<TodoDetails> {
        return combine(
            firestoreClient.getTodoItem(todoId),
            firestoreClient.getLabelItems(),
            firestoreClient.getReminderItems()
        ) { todos, labels, reminders ->
            val labelsMap = labels.associateBy { it.id }
            val remindersMap = reminders.associateBy { it.id }

            todos.toTodoDetails(
                getLabel = { labelId -> labelsMap[labelId]?.toLabelDetails() ?: LabelDetails() },
                getReminder = { reminderId -> remindersMap[reminderId]?.toReminderDetails() ?: ReminderDetails() }
            )
        }
    }

    fun getTodos(): Flow<List<TodoDetails>> {
        return combine(
            firestoreClient.getTodoItems(),
            firestoreClient.getLabelItems(),
            firestoreClient.getReminderItems()
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