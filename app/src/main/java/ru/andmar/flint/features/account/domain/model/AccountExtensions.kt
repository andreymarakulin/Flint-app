package ru.andmar.flint.features.account.domain.model

import ru.andmar.flint.features.account.data.model.AuthItem

fun isSignInAction(authDetails: AuthDetails, agreement: Boolean): Boolean {
    return with(authDetails) {
        email.isNotBlank() && password.isNotBlank() && agreement
    }
}

fun isChangeEmailAction(email: String): Boolean {
    return email.isNotBlank()
}

fun isEditPasswordAction(password: String): Boolean {
    return password.isNotBlank()
}