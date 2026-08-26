package ru.andmar.flint.features.account.ui.signUp

import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.account.domain.model.AuthDetails

data class SignUpUiState(
    val authDetails: AuthDetails = AuthDetails(),
    val flintActions: FlintActions = FlintActions.Default,
    val isUserAgreement: Boolean = false,
    val isAction: Boolean = false
)