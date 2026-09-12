package ru.andmar.flint.features.account.ui.edit.email

sealed interface UpdateEmailScreenActions {
    data class UpdateEmail(val email: String): UpdateEmailScreenActions
    object ChangeEmail: UpdateEmailScreenActions
}