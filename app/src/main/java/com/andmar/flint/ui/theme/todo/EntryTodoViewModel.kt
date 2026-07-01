package com.andmar.flint.ui.theme.todo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.andmar.flint.FlintActions
import com.andmar.flint.data.DefaultFlintRepository
import com.andmar.flint.firebase.TodoItem

class EntryTodoViewModel(
    private val flintRepository: DefaultFlintRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val noteId: String = savedStateHandle.toRoute<EntryTodoScreenRoute>().noteId

    var entryTodoUiState by mutableStateOf(EntryTodoUiState())
        private set

    fun onActions(entryTodoActions: EntryTodoActions) {
        when(entryTodoActions) {
            is EntryTodoActions.UpdateTodoDetails -> {
                updateTodoDetails(entryTodoActions.todoDetails)
            }
            is EntryTodoActions.CreateTodo -> {createTodo() }
        }
    }

    private fun updateTodoDetails(todoDetails: TodoDetails) {
        entryTodoUiState = EntryTodoUiState(
            todoDetails = todoDetails,
            isAction = isTodoAction(todoDetails)
        )
    }

    private fun updateFlintActions(flintActions: FlintActions) {

    }

    private fun createTodo() {
        flintRepository.createTodo(
            todoItem = entryTodoUiState.todoDetails
                .copy(noteId = noteId).toTodoItem()
        ) { updateFlintActions(it) }
    }
}

data class EntryTodoUiState(
    val todoDetails: TodoDetails = TodoDetails(),
    val isAction: Boolean = false
)

sealed interface EntryTodoActions {
    data class UpdateTodoDetails(val todoDetails: TodoDetails): EntryTodoActions
    object CreateTodo: EntryTodoActions
}

data class TodoDetails(
    val id: String = "",
    val noteId: String = "",
    val title: String = "",
    val text: String = "",
    val fix: Boolean = false,
    val done: Boolean = false,
    val highlight: Boolean = false,
    val updateTime: Long = 0
)

fun isTodoAction(todoDetails: TodoDetails): Boolean {
    return todoDetails.text.isNotBlank()
}

fun TodoDetails.toTodoItem(): TodoItem = TodoItem(
    id = id,
    noteId = noteId,
    title = title,
    text = text,
    fix = fix,
    done = done,
    highlight = highlight,
    updateTime = System.currentTimeMillis()
)


fun TodoItem.toTodoDetails(): TodoDetails = TodoDetails(
    id = id,
    noteId = noteId,
    title = title,
    text = text,
    fix = fix,
    done = done,
    highlight = highlight,
    updateTime = updateTime
)