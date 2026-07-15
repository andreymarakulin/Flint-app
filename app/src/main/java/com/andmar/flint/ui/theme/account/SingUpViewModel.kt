package com.andmar.flint.ui.theme.account

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andmar.flint.FlintActions
import com.andmar.flint.FlintRepository
import kotlinx.coroutines.launch

class SingUpViewModel(
    private val flintRepository: FlintRepository
): ViewModel() {

    var signUpUiState by mutableStateOf(SignUpUiState())
        private set

    fun onActions(signUpActions: SignUpActions) {
        when(signUpActions) {
            is SignUpActions.UpdateAuthDetails -> {
                updateAuthDetails(signUpActions.authDetails)
            }
            is SignUpActions.UpdateUserAgreement -> {
                updateUserAgreement(signUpUiState.isUserAgreement)
            }
            is SignUpActions.SignUp -> { createUser() }
            is SignUpActions.DismissError -> {
                updateFlintActions(FlintActions.Default)
            }
        }
    }

    private fun updateAuthDetails(authDetails: AuthDetails) {
        signUpUiState = SignUpUiState(
            authDetails = authDetails,
            isAction = isSignInAction(authDetails, true)//signUpUiState.isUserAgreement)
        )
    }

    private fun updateFlintActions(flintActions: FlintActions) {
        signUpUiState = signUpUiState.copy(
            flintActions = flintActions
        )
    }

    private fun createUser() {
        viewModelScope.launch {
            try {
                flintRepository.createUser(signUpUiState.authDetails)
            } catch (e: Exception) {

            }
        }
    }

    private fun updateUserAgreement(isUserAgreement: Boolean) {
        signUpUiState = signUpUiState.copy(
            isUserAgreement = isUserAgreement
        )
    }
}

data class SignUpUiState(
    val authDetails: AuthDetails = AuthDetails(),
    val flintActions: FlintActions = FlintActions.Default,
    val isUserAgreement: Boolean = false,
    val isAction: Boolean = false
)

sealed interface SignUpActions {
    data class UpdateAuthDetails(val authDetails: AuthDetails): SignUpActions
    data class UpdateUserAgreement(val isUserAgreement: Boolean): SignUpActions
    object SignUp: SignUpActions
    object DismissError: SignUpActions
}