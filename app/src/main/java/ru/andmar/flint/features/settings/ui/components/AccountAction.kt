package ru.andmar.flint.features.settings.ui.components

import ru.andmar.flint.features.settings.domain.UserDetails

sealed interface AccountAction {

    data class EditEmail(val userDetails: UserDetails): AccountAction
    data class EditPassword(val userDetails: UserDetails): AccountAction
    data class SignOut(val userDetails: UserDetails): AccountAction
    data class DeleteAccount(val userDetails: UserDetails): AccountAction
}