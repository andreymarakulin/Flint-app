package ru.andmar.flint.features.label.ui.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.label.domain.model.isLabelAction
import ru.andmar.flint.features.label.domain.usecase.EditLabelUseCase
import ru.andmar.flint.navigation.NavigationRoutes

class EditLabelViewModel(
    private val editLabelUseCase: EditLabelUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val labelId: String = savedStateHandle
        .toRoute<NavigationRoutes.EditLabelScreenRoute>()
        .labelId

    private val _editLabelUiState = MutableStateFlow(EditLabelUiState())
    val editLabelUiState: StateFlow<EditLabelUiState> = _editLabelUiState

    init {
        viewModelScope.launch {
            _editLabelUiState.update {
                it.copy(
                    labelDetails = editLabelUseCase.getLabelDetails(labelId)
                )
            }
        }
    }

    fun onActions(editLabelScreenActions: EditLabelScreenActions) {
        when (editLabelScreenActions) {
            is EditLabelScreenActions.UpdateLabelScreenDetails -> {
                _editLabelUiState.update {
                    it.copy(
                        labelDetails = editLabelScreenActions.labelDetails,
                        isAction = isLabelAction(editLabelScreenActions.labelDetails)
                    )
                }
            }

            is EditLabelScreenActions.EditLabel -> {
                flintAction {
                    editLabelUseCase.editLabel(_editLabelUiState.value.labelDetails)
                }
            }

            is EditLabelScreenActions.DismissError -> {
                _editLabelUiState.update {
                    it.copy(flintActions = FlintActions.Default)
                }
            }
        }
    }

    private fun flintAction(action: suspend () -> Result<Unit>) {
        viewModelScope.launch {
            _editLabelUiState.update {
                it.copy(flintActions = FlintActions.Loading)
            }
            action().onSuccess {
                _editLabelUiState.update {
                    it.copy(flintActions = FlintActions.Success)
                }
            }.onFailure { e ->
                _editLabelUiState.update {
                    it.copy(flintActions = FlintActions.Error(e.message ?: "Error"))
                }
            }
        }
    }
}