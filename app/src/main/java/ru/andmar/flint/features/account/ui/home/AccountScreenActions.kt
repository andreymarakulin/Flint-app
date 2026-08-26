package ru.andmar.flint.features.account.ui.home

sealed interface AccountScreenActions {
    object EditEmail: AccountScreenActions
    object EditPassword: AccountScreenActions
    object SignOut: AccountScreenActions
    object DeleteAccount: AccountScreenActions
}