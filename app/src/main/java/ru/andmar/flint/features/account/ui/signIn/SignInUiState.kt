package ru.andmar.flint.features.account.ui.signIn

import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.account.domain.model.AuthDetails

data class SignInUiState(
    val authDetails: AuthDetails = AuthDetails(),
    val flintActions: FlintActions = FlintActions.Default,
    val isAction: Boolean = false
)