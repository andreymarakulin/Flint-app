package com.andmar.flint.ui.theme.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.andmar.flint.FlintActions
import com.andmar.flint.data.DefaultFlintRepository
import com.andmar.flint.ui.theme.note.NoteDetails
import com.andmar.flint.ui.theme.note.toNoteDetails
import com.andmar.flint.ui.theme.note.toNoteItem
import com.andmar.flint.ui.theme.todo.TodoDetails
import com.andmar.flint.ui.theme.todo.TodoDetailsState
import com.andmar.flint.ui.theme.todo.toTodoDetails
import com.andmar.flint.ui.theme.todo.toTodoItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DetailsViewModel(
    private val flintRepository: DefaultFlintRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val noteId: String = savedStateHandle.toRoute<DetailsScreenRoute>().noteId

    val noteDetailsState: StateFlow<NoteDetailsState> =
        flintRepository.getNoteById(noteId).map { noteItem ->
            NoteDetailsState(noteItem.toNoteDetails())
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NoteDetailsState()
        )

    val todoDetailsState: StateFlow<TodoDetailsState> =
        flintRepository.getTodos().map { todoItems ->
            TodoDetailsState(
                todoItems.filter { it.noteId == noteId }.map { todoItem ->
                    todoItem.toTodoDetails()
                }
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

        flintRepository.editNote(
            noteItem = noteDetails
                .copy(fix = !noteDetails.fix)
                .toNoteItem()
        ) { updateFlintActions(it) }
    }

    private fun doneNote() {
        val noteDetails = noteDetailsState.value.noteDetails

        flintRepository.editNote(
            noteItem = noteDetails
                .copy(done = !noteDetails.done)
                .toNoteItem()
        ) { updateFlintActions(it) }
    }

    private fun highlightNote() {
        val noteDetails = noteDetailsState.value.noteDetails

        flintRepository.editNote(
            noteItem = noteDetails
                .copy(highlight = !noteDetails.highlight)
                .toNoteItem()
        ) { updateFlintActions(it) }
    }

    private fun deleteNote() {
        flintRepository.deleteNote(
            noteItem = noteDetailsState.value.noteDetails.toNoteItem()
        ) { updateFlintActions(it) }
    }

    private fun fixTodo() {
        val todoDetails = detailsUiState.selectedTodoDetails

        flintRepository.editTodo(
            todoItem = todoDetails.copy(
                fix = !todoDetails.fix
            ).toTodoItem()
        ) { updateFlintActions(it) }
    }

    private fun doneTodo() {
        val todoDetails = detailsUiState.selectedTodoDetails

        flintRepository.editTodo(
            todoItem = todoDetails.copy(
                done = !todoDetails.done
            ).toTodoItem()
        ) { updateFlintActions(it) }
    }

    private fun highlightTodo() {
        val todoDetails = detailsUiState.selectedTodoDetails

        flintRepository.editTodo(
            todoItem = todoDetails.copy(
                highlight = !todoDetails.highlight
            ).toTodoItem()
        ) { updateFlintActions(it) }
    }

    private fun deleteTodo() {
        flintRepository.deleteTodo(
            todoItem = detailsUiState.selectedTodoDetails.toTodoItem()
        ) { updateFlintActions(it) }
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