package ru.andmar.flint.features.account.ui.home

import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.settings.domain.UserDetails

data class AccountUiState(
    val userDetails: UserDetails = UserDetails(),
    val flintActions: FlintActions = FlintActions.Default
)