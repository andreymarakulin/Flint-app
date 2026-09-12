package ru.andmar.flint.features.account.ui.edit.email

import ru.andmar.flint.core.ui.FlintActions

data class UpdateEmailUiState(
    val email: String = "",
    val flintActions: FlintActions = FlintActions.Default,
    val isChangeEmailAction: Boolean = false
)