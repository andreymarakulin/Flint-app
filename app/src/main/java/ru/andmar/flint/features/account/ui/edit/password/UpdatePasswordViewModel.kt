package ru.andmar.flint.features.account.ui.edit.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.account.domain.model.isEditPasswordAction
import ru.andmar.flint.features.account.domain.usecase.EditPasswordUseCase

class UpdatePasswordViewModel(private val editPasswordUseCase: EditPasswordUseCase): ViewModel() {

    private val _updatePasswordUiState = MutableStateFlow(UpdatePasswordUiState())
    val updatePasswordUiState: StateFlow<UpdatePasswordUiState> = _updatePasswordUiState

    fun onActions(updatePasswordScreenActions: UpdatePasswordScreenActions) {
        when(updatePasswordScreenActions) {
            is UpdatePasswordScreenActions.UpdatePassword -> {
                _updatePasswordUiState.update {
                    it.copy(
                        password = updatePasswordScreenActions.password,
                        isEditPasswordAction = isEditPasswordAction(updatePasswordScreenActions.password)
                    )
                }
            }
            is UpdatePasswordScreenActions.EditPassword -> {
                flintAction {
                    editPasswordUseCase.editPassword(_updatePasswordUiState.value.password)
                }
            }
        }
    }

    private fun flintAction(action: suspend () -> Result<Unit>) {
        viewModelScope.launch {
            _updatePasswordUiState.update {
                it.copy(flintActions = FlintActions.Loading)
            }
            action().onSuccess {
                _updatePasswordUiState.update {
                    it.copy(flintActions = FlintActions.Success)
                }
            }.onFailure { e ->
                _updatePasswordUiState.update {
                    it.copy(flintActions = FlintActions.Error(e.message ?: "Error"))
                }
            }
        }
    }
}