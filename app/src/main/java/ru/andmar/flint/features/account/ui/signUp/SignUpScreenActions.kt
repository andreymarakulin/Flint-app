package ru.andmar.flint.features.account.ui.signUp

import ru.andmar.flint.features.account.domain.model.AuthDetails

sealed interface SignUpScreenActions {
    data class UpdateAuthDetails(val authDetails: AuthDetails): SignUpScreenActions
    //data class UpdateUserAgreement(val isUserAgreement: Boolean): SignUpScreenActions
    object SignUpScreen: SignUpScreenActions
    object DismissError: SignUpScreenActions
}