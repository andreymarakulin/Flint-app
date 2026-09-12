package ru.andmar.flint.features.account.ui.edit.password

import ru.andmar.flint.core.ui.FlintActions

data class UpdatePasswordUiState(
    val password: String = "",
    val flintActions: FlintActions = FlintActions.Default,
    val isEditPasswordAction: Boolean = false
)