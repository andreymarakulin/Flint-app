package ru.andmar.flint.features.account.ui.edit.password

sealed interface UpdatePasswordScreenActions {
    data class UpdatePassword(val password: String): UpdatePasswordScreenActions
    object EditPassword: UpdatePasswordScreenActions
}