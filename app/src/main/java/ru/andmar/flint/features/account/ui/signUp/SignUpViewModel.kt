package ru.andmar.flint.features.account.ui.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.account.domain.model.isSignInAction
import ru.andmar.flint.features.account.domain.usecase.SignUpUseCase

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase
): ViewModel() {

    private val _signUpUiState = MutableStateFlow(SignUpUiState())
    val signUpUiState: StateFlow<SignUpUiState> = _signUpUiState

    fun onActions(signUpScreenActions: SignUpScreenActions) {
        when(signUpScreenActions) {
            is SignUpScreenActions.UpdateAuthDetails -> {
                _signUpUiState.update {
                    it.copy(
                        authDetails = signUpScreenActions.authDetails,
                        isAction = isSignInAction(signUpScreenActions.authDetails, true)
                    )
                }
            }
            is SignUpScreenActions.SignUpScreen -> {
                flintActions {
                    signUpUseCase.createUser(_signUpUiState.value.authDetails)
                        .onSuccess { signUpUseCase.createUser() }
                }
            }
            is SignUpScreenActions.DismissError -> {
                _signUpUiState.update {
                    it.copy(flintActions = FlintActions.Default)
                }
            }
        }
    }

    private fun flintActions(action: suspend () -> Result<Unit>) {
        viewModelScope.launch {
            _signUpUiState.update {
                it.copy(flintActions = FlintActions.Loading)
            }
            action()
                .onSuccess { _signUpUiState.update { it.copy(flintActions = FlintActions.Success) } }
                .onFailure { e -> _signUpUiState.update { it.copy(flintActions = FlintActions.Error(e.message ?: "Error")) } }
        }
    }
}