package ru.andmar.flint.features.account.ui.signIn

import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.account.domain.model.AuthDetails

sealed interface SignInScreenActions {
    data class UpdateAuthDetails(val authDetails: AuthDetails): SignInScreenActions
    data class UpdateFlintScreenActions(val flintActions: FlintActions): SignInScreenActions
    object SignInScreen: SignInScreenActions
    object DismissError: SignInScreenActions
}

