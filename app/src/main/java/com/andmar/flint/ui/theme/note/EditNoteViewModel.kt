package com.andmar.flint.ui.theme.note

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.andmar.flint.FlintActions
import com.andmar.flint.FlintRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditNoteViewModel(
    private val flintRepository: FlintRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val noteId: String = savedStateHandle.toRoute<EditNoteScreenRoute>().noteId

    var editNoteUiState by mutableStateOf(EditNoteUiState())
        private set

    init {
        viewModelScope.launch {
            editNoteUiState = EditNoteUiState(
                noteDetails = flintRepository.getNoteById(noteId)
                    .first()
            )
        }
    }

    fun onActions(editNoteActions: EditNoteActions) {
        when(editNoteActions) {
            is EditNoteActions.UpdateNoteDetails -> {
                updateNoteDetails(editNoteActions.noteDetails)
            }
            is EditNoteActions.EditNote -> { editNote() }
            is EditNoteActions.DismissError -> {
                updateFlintActions(FlintActions.Default)
            }
        }
    }

    private fun updateNoteDetails(noteDetails: NoteDetails) {
        editNoteUiState = EditNoteUiState(
            noteDetails = noteDetails,
            isAction = isNoteAction(noteDetails)
        )
    }

    private fun updateFlintActions(flintActions: FlintActions) {
        editNoteUiState = editNoteUiState.copy(
            flintActions = flintActions
        )
    }

    private fun editNote() {
        viewModelScope.launch {
            updateFlintActions(FlintActions.Loading)
            try {
                flintRepository.editNote(
                    editNoteUiState.noteDetails
                )
                updateFlintActions(FlintActions.Success)
            } catch (e: Exception) {
                updateFlintActions(FlintActions.Error(e.message ?: "Error"))
            }
        }
    }
}

data class EditNoteUiState(
    val noteDetails: NoteDetails = NoteDetails(),
    val flintActions: FlintActions = FlintActions.Default,
    val isAction: Boolean = false
)

sealed interface EditNoteActions {
    data class UpdateNoteDetails(val noteDetails: NoteDetails): EditNoteActions
    object EditNote: EditNoteActions
    object DismissError: EditNoteActions
}