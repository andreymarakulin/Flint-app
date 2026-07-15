package com.andmar.flint.ui.theme.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.andmar.flint.FlintActions
import com.andmar.flint.FlintRepository
import com.andmar.flint.ui.theme.note.NoteDetails
import com.andmar.flint.ui.theme.todo.TodoDetails
import com.andmar.flint.ui.theme.todo.TodoDetailsState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val flintRepository: FlintRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val noteId: String = savedStateHandle.toRoute<DetailsScreenRoute>().noteId

    val noteDetailsState: StateFlow<NoteDetailsState> =
        flintRepository.getNoteById(noteId).map { noteDetails ->
            NoteDetailsState(noteDetails)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NoteDetailsState()
        )

    val todoDetailsState: StateFlow<TodoDetailsState> =
        flintRepository.getTodos().map { todoItems ->
            TodoDetailsState(
                todoItems.filter { it.noteId == noteId }
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = TodoDetailsState()
        )

    var detailsUiState by mutableStateOf(DetailsUiState())
        private set

    fun onActions(detailsActions: DetailsActions) {
        when(detailsActions) {
            is DetailsActions.UpdateSelectedTodoDetails -> {
                updateSelectedTodoDetails(detailsActions.todoDetails)
            }
            is DetailsActions.FixNote -> fixNote()
            is DetailsActions.DoneNote -> doneNote()
            is DetailsActions.HighlightNote -> highlightNote()
            is DetailsActions.DeleteNote -> deleteNote()
            is DetailsActions.FixTodo -> fixTodo()
            is DetailsActions.DoneTodo -> doneTodo()
            is DetailsActions.HighlightTodo -> highlightTodo()
            is DetailsActions.DeleteTodo -> deleteTodo()
            is DetailsActions.DismissError -> {
                updateFlintActions(FlintActions.Default)
            }
        }
    }

    private fun updateSelectedTodoDetails(todoDetails: TodoDetails) {
        detailsUiState = detailsUiState.copy(
            selectedTodoDetails = todoDetails
        )
    }
    private fun updateFlintActions(flintActions: FlintActions) {
        detailsUiState = detailsUiState.copy(
            flintActions = flintActions
        )
    }

    private fun fixNote() {
        val noteDetails = noteDetailsState.value.noteDetails

        viewModelScope.launch {
            updateFlintActions(FlintActions.Loading)
            try {
                flintRepository.editNote(
                    noteDetails.copy(fix = !noteDetails.fix)
                )
                updateFlintActions(FlintActions.Success)
            } catch (e: Exception) {
                updateFlintActions(FlintActions.Error(e.message ?: "Error"))
            }
        }
    }

    private fun doneNote() {
        val noteDetails = noteDetailsState.value.noteDetails

        viewModelScope.launch {
            updateFlintActions(FlintActions.Loading)
            try {
                flintRepository.editNote(
                    noteDetails.copy(done = !noteDetails.done)
                )
                updateFlintActions(FlintActions.Success)
            } catch (e: Exception) {
                updateFlintActions(FlintActions.Error(e.message ?: "Error"))
            }
        }
    }

    private fun highlightNote() {
        viewModelScope.launch {
            updateFlintActions(FlintActions.Loading)
            val noteDetails = noteDetailsState.value.noteDetails
            try {
                flintRepository.editNote(
                    noteDetails.copy(highlight = !noteDetails.highlight)
                )
                updateFlintActions(FlintActions.Success)
            } catch (e: Exception) {
                updateFlintActions(FlintActions.Error(e.message ?: "Error"))
            }
        }
    }

    private fun deleteNote() {
        viewModelScope.launch {
            updateFlintActions(FlintActions.Loading)
            try {
                flintRepository.deleteNote(noteDetailsState.value.noteDetails)
                updateFlintActions(FlintActions.Success)
            } catch (e: Exception) {
                updateFlintActions(FlintActions.Error(e.message ?: "Error"))
            }
        }
    }

    private fun fixTodo() {
        val todoDetails = detailsUiState.selectedTodoDetails

        viewModelScope.launch {
            updateFlintActions(FlintActions.Loading)
            try {
                flintRepository.editTodo(
                    todoDetails.copy(fix = !todoDetails.fix)
                )
                updateFlintActions(FlintActions.Success)
            } catch (e: Exception) {
                updateFlintActions(FlintActions.Error(e.message ?: "Error"))
            }
        }
    }

    private fun doneTodo() {
        val todoDetails = detailsUiState.selectedTodoDetails

        viewModelScope.launch {
            updateFlintActions(FlintActions.Loading)
            try {
                flintRepository.editTodo(
                    todoDetails.copy(done = !todoDetails.done)
                )
                updateFlintActions(FlintActions.Success)
            } catch (e: Exception) {
                updateFlintActions(FlintActions.Error(e.message ?: "Error"))
            }
        }
    }

    private fun highlightTodo() {
        val todoDetails = detailsUiState.selectedTodoDetails

        viewModelScope.launch {
            updateFlintActions(FlintActions.Loading)
            try {
                flintRepository.editTodo(
                    todoDetails.copy(highlight = !todoDetails.highlight)
                )
                updateFlintActions(FlintActions.Success)
            } catch (e: Exception) {
                updateFlintActions(FlintActions.Error(e.message ?: "Error"))
            }
        }
    }

    private fun deleteTodo() {
        viewModelScope.launch {
            updateFlintActions(FlintActions.Loading)
            try {
                flintRepository.deleteTodo(
                    detailsUiState.selectedTodoDetails)
                updateFlintActions(FlintActions.Success)
            } catch (e: Exception) {
                updateFlintActions(FlintActions.Error(e.message ?: "Error"))
            }
        }
    }
}

data class NoteDetailsState(val noteDetails: NoteDetails = NoteDetails())
data class DetailsUiState(
    val selectedTodoDetails: TodoDetails = TodoDetails(),
    val flintActions: FlintActions = FlintActions.Default
)

sealed interface DetailsActions {
    data class UpdateSelectedTodoDetails(val todoDetails: TodoDetails): DetailsActions
    object FixNote: DetailsActions
    object DoneNote: DetailsActions
    object HighlightNote: DetailsActions
    object DeleteNote: DetailsActions
    object FixTodo: DetailsActions
    object DoneTodo: DetailsActions
    object HighlightTodo: DetailsActions
    object DeleteTodo: DetailsActions
    object DismissError: DetailsActions
}