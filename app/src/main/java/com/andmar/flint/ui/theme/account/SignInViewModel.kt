package com.andmar.flint.ui.theme.account

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.andmar.flint.FlintActions
import com.andmar.flint.data.DefaultFlintRepository
import com.andmar.flint.firebase.AuthItem

class SignInViewModel(
    private val flintRepository: DefaultFlintRepository
): ViewModel() {

    var signInUiState by mutableStateOf(SignInUiState())
        private set

    fun onActions(signInActions: SignInActions) {
        when(signInActions) {
            is SignInActions.UpdateAuthDetails -> {
                updateAuthDetails(signInActions.authDetails)
            }
            is SignInActions.UpdateFlintActions -> {
                updateFlintActions(signInActions.flintActions)
            }
            is SignInActions.SignIn -> { signIn() }
            is SignInActions.DismissError -> {
                updateFlintActions(FlintActions.Default)
            }
        }
    }

    private fun updateAuthDetails(authDetails: AuthDetails) {
        signInUiState = SignInUiState(
            authDetails = authDetails,
            isAction = isSignInAction(authDetails, true)
        )
    }

    private fun updateFlintActions(flintActions: FlintActions) {
        signInUiState = signInUiState.copy(
            flintActions = flintActions
        )
    }

    private fun signIn() {
        flintRepository.signIn(
            authItem = signInUiState.authDetails.toAuthItem(),
        ) { updateFlintActions(it) }
    }
}

data class SignInUiState(
    val authDetails: AuthDetails = AuthDetails(),
    val flintActions: FlintActions = FlintActions.Default,
    val isAction: Boolean = false
)

sealed interface SignInActions {
    data class UpdateAuthDetails(val authDetails: AuthDetails): SignInActions
    data class UpdateFlintActions(val flintActions: FlintActions): SignInActions
    object SignIn: SignInActions
    object DismissError: SignInActions
}

data class AuthDetails(
    val email: String = "",
    val password: String = ""
)

fun isSignInAction(authDetails: AuthDetails, agreement: Boolean): Boolean {
    return with(authDetails) {
        email.isNotBlank() && password.isNotBlank() && agreement
    }
}

fun AuthDetails.toAuthItem(): AuthItem = AuthItem(
    email = email,
    password = password
)