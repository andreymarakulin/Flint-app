package ru.andmar.flint.features.note.ui.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.note.domain.model.isNoteAction
import ru.andmar.flint.features.note.domain.usecase.EditNoteUseCase
import ru.andmar.flint.navigation.NavigationRoutes

class EditNoteViewModel(
    private val editNoteUseCase: EditNoteUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val noteId: String =
        savedStateHandle.toRoute<NavigationRoutes.EditNoteScreenRoute>().noteId

    private val _editNoteUiState = MutableStateFlow(EditNoteUiState())
    val editNoteUiState: StateFlow<EditNoteUiState> = _editNoteUiState

    init {
        viewModelScope.launch {
            _editNoteUiState.update {
                it.copy(noteDetails = editNoteUseCase.getNoteById(noteId))
            }
        }
    }

    fun onActions(editNoteScreenActions: EditNoteScreenActions) {
        when (editNoteScreenActions) {
            is EditNoteScreenActions.UpdateNoteScreenDetails -> {
                _editNoteUiState.update {
                    it.copy(
                        noteDetails = editNoteScreenActions.noteDetails,
                        isAction = isNoteAction(editNoteScreenActions.noteDetails)
                    )
                }
            }
            is EditNoteScreenActions.NavBack -> {
                navBack(editNoteScreenActions.onNavBack)
            }
            is EditNoteScreenActions.EditNoteScreen -> {
                flintActions {
                    editNoteUseCase.editNote(_editNoteUiState.value.noteDetails)
                }
            }
            is EditNoteScreenActions.DismissError -> {
                _editNoteUiState.update {
                    it.copy(flintActions = FlintActions.Default)
                }
            }
        }
    }

    private fun flintActions(action: suspend () -> Result<Unit>) {
        viewModelScope.launch {
            _editNoteUiState.update {
                it.copy(flintActions = FlintActions.Loading)
            }
            action().onSuccess {
                _editNoteUiState.update {
                    it.copy(flintActions = FlintActions.Success)
                }
            }.onFailure { e ->
                _editNoteUiState.update {
                    it.copy(flintActions = FlintActions.Error(e.message ?: "Error"))
                }
            }
        }
    }

    private fun navBack(onNavBack: () -> Unit) {
        /*
        if (editNoteUiState.isEdit) {
            onNavBack()
        } else if (editNoteUiState.isAction) {
            editNote()
        } else onNavBack()

         */
    }
}