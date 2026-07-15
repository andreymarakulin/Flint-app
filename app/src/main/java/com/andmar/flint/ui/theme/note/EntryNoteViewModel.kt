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
import kotlinx.coroutines.launch

class EntryNoteViewModel(
    private val flintRepository: FlintRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val categoryId: String = savedStateHandle.toRoute<EntryNoteScreenRoute>().categoryId

    var entryNoteUiState by mutableStateOf(EntryNoteUiState())
        private set

    fun onActions(entryNoteActions: EntryNoteActions) {
        when(entryNoteActions) {
            is EntryNoteActions.UpdateNoteDetails -> {
                updateNoteDetails(entryNoteActions.noteDetails)
            }
            is EntryNoteActions.CreateNote -> { createNote() }
            is EntryNoteActions.DismissError -> {
                updateFlintActions(FlintActions.Default)
            }
        }
    }

    private fun updateNoteDetails(noteDetails: NoteDetails) {
        entryNoteUiState = EntryNoteUiState(
            noteDetails = noteDetails,
            isAction = isNoteAction(noteDetails)
        )
    }

    private fun updateFlintActions(flintActions: FlintActions) {
        entryNoteUiState = entryNoteUiState.copy(
            flintActions = flintActions
        )
    }

    private fun createNote() {
        viewModelScope.launch {
            updateFlintActions(FlintActions.Loading)
            try {
                flintRepository.createNote(
                    entryNoteUiState.noteDetails
                        .copy(categoryId = categoryId)
                )
                updateFlintActions(FlintActions.Success)
            } catch (e: Exception) {
                updateFlintActions(FlintActions.Error(e.message ?: "Error"))
            }
        }
    }
}

data class EntryNoteUiState(
    val noteDetails: NoteDetails = NoteDetails(),
    val flintActions: FlintActions = FlintActions.Default,
    val isAction: Boolean = false
)

sealed interface EntryNoteActions {
    data class UpdateNoteDetails(val noteDetails: NoteDetails): EntryNoteActions
    object CreateNote: EntryNoteActions
    object DismissError: EntryNoteActions
}

data class NoteDetails(
    val id: String = "",
    val categoryId: String = "",
    val title: String = "",
    val text: String = "",
    val fix: Boolean = false,
    val done: Boolean = false,
    val highlight: Boolean = false,
    val updateTime: Long = 0
)
fun isNoteAction(noteDetails: NoteDetails): Boolean {
    return with(noteDetails) {
        title.isNotBlank() || text.isNotBlank()
    }
}