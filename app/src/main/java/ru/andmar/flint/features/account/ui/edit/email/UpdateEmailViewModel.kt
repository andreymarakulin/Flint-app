package ru.andmar.flint.features.account.ui.edit.email

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.account.domain.model.isChangeEmailAction
import ru.andmar.flint.features.account.domain.usecase.ChangeEmailUseCase

class UpdateEmailViewModel(private val changeEmailUseCase: ChangeEmailUseCase): ViewModel() {

    private val _updateEmailUiState = MutableStateFlow(UpdateEmailUiState())
    val updateEmailUiState: StateFlow<UpdateEmailUiState> = _updateEmailUiState


    fun onActions(updateEmailScreenActions: UpdateEmailScreenActions) {
        when(updateEmailScreenActions) {
            is UpdateEmailScreenActions.UpdateEmail -> {
                _updateEmailUiState.update {
                    it.copy(
                        email = updateEmailScreenActions.email,
                        isChangeEmailAction = isChangeEmailAction(updateEmailScreenActions.email)
                    )
                }
            }
            is UpdateEmailScreenActions.ChangeEmail -> {
                flintAction {
                    changeEmailUseCase.changeEmail(_updateEmailUiState.value.email)
                }
            }
        }
    }

    private fun flintAction(action: suspend () -> Result<Unit>) {
        viewModelScope.launch {
            _updateEmailUiState.update {
                it.copy(flintActions = FlintActions.Loading)
            }
            action().onSuccess {
                _updateEmailUiState.update {
                    it.copy(flintActions = FlintActions.Success)
                }
            }.onFailure { e ->
                _updateEmailUiState.update {
                    it.copy(flintActions = FlintActions.Error(e.message ?: "Error"))
                }
            }
        }
    }
}