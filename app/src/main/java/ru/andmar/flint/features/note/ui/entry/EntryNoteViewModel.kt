package ru.andmar.flint.features.note.ui.entry


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
import ru.andmar.flint.features.note.domain.usecase.EntryNoteUseCase
import ru.andmar.flint.navigation.NavigationRoutes

class EntryNoteViewModel(
    private val entryNoteUseCase: EntryNoteUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val categoryId: String = savedStateHandle
        .toRoute<NavigationRoutes.EntryNoteScreenRoute>()
        .categoryId

    private val _entryNoteUiState = MutableStateFlow(EntryNoteUiState())
    val entryNoteUiState: StateFlow<EntryNoteUiState> = _entryNoteUiState

    fun onActions(entryNoteScreenActions: EntryNoteScreenActions) {
        when(entryNoteScreenActions) {
            is EntryNoteScreenActions.UpdateNoteScreenDetails -> {
                _entryNoteUiState.update {
                    it.copy(
                        noteDetails = entryNoteScreenActions.noteDetails,
                        isAction = isNoteAction(entryNoteScreenActions.noteDetails)
                    )
                }
            }
            is EntryNoteScreenActions.CreateNoteScreen -> {
                flintActions {
                    entryNoteUseCase.createNote(
                        noteDetails = _entryNoteUiState.value.noteDetails,
                        categoryId = categoryId
                    )
                }
            }
            is EntryNoteScreenActions.DismissError -> {
                _entryNoteUiState.update {
                    it.copy(flintActions = FlintActions.Default)
                }
            }
        }
    }

    private fun flintActions(action: suspend () -> Result<Unit>) {
        viewModelScope.launch {
            _entryNoteUiState.update {
                it.copy(flintActions = FlintActions.Loading)
            }
            action().onSuccess {
                _entryNoteUiState.update {
                    it.copy(flintActions = FlintActions.Success)
                }
            }.onFailure { e ->
                _entryNoteUiState.update {
                    it.copy(flintActions = FlintActions.Error(e.message ?: "Error"))
                }
            }
        }
    }
}