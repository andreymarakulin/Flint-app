package ru.andmar.flint.features.label.ui.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.label.domain.model.isLabelAction
import ru.andmar.flint.features.label.domain.usecase.EntryLabelUseCase

class EntryLabelViewModel(
    private val entryLabelUseCase: EntryLabelUseCase
): ViewModel() {

    private val _entryLabelUiState = MutableStateFlow(EntryLabelUiState())
    val entryLabelUiState: StateFlow<EntryLabelUiState> = _entryLabelUiState

    fun onActions(entryLabelScreenActions: EntryLabelScreenActions) {
        when (entryLabelScreenActions) {
            is EntryLabelScreenActions.UpdateLabelScreenDetails -> {
                _entryLabelUiState.update {
                    it.copy(
                        labelDetails = entryLabelScreenActions.labelDetails,
                        isAction = isLabelAction(entryLabelScreenActions.labelDetails)
                    )
                }
            }
            is EntryLabelScreenActions.CreateLabel -> {
                flintAction {
                    entryLabelUseCase.entryLabel(_entryLabelUiState.value.labelDetails)
                }
            }
            is EntryLabelScreenActions.DismissError -> {
                _entryLabelUiState.update {
                    it.copy(flintActions = FlintActions.Default)
                }
            }
        }
    }

    private fun flintAction(action: suspend () -> Result<Unit>) {
        viewModelScope.launch {
            _entryLabelUiState.update {
                it.copy(flintActions = FlintActions.Loading)
            }
            action().onSuccess {
                _entryLabelUiState.update {
                    it.copy(flintActions = FlintActions.Success)
                }
            }.onFailure { e ->
                _entryLabelUiState.update {
                    it.copy(flintActions = FlintActions.Error(e.message ?: "Error"))
                }
            }
        }
    }
}