package ru.andmar.flint.features.account.ui.signIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.account.domain.model.isSignInAction
import ru.andmar.flint.features.account.domain.usecase.SignInUseCase

class SignInViewModel(
    private val signInUseCase: SignInUseCase
): ViewModel() {

    private val _signInUiState = MutableStateFlow(SignInUiState())
    val signInUiState: StateFlow<SignInUiState> = _signInUiState

    fun onActions(signInScreenActions: SignInScreenActions) {
        when (signInScreenActions) {
            is SignInScreenActions.UpdateAuthDetails -> {
                _signInUiState.update {
                    it.copy(
                        authDetails = signInScreenActions.authDetails,
                        isAction = isSignInAction(signInScreenActions.authDetails, true)
                    )
                }
            }

            is SignInScreenActions.UpdateFlintScreenActions -> {
                _signInUiState.update {
                    it.copy(flintActions = signInScreenActions.flintActions)
                }
            }

            is SignInScreenActions.SignInScreen -> {
                flintActions {
                    signInUseCase.signIn(_signInUiState.value.authDetails)
                }
            }

            is SignInScreenActions.DismissError -> {
                _signInUiState.update {
                    it.copy(flintActions = FlintActions.Default)
                }
            }
        }
    }

    private fun flintActions(action: suspend () -> Result<Unit>) {
        viewModelScope.launch {
            _signInUiState.update {
                it.copy(flintActions = FlintActions.Loading)
            }
            action()
                .onSuccess { _signInUiState.update { it.copy(flintActions = FlintActions.Success) } }
                .onFailure { e -> _signInUiState.update { it.copy(flintActions = FlintActions.Error(e.message ?: "Error")) } }
        }
    }
}