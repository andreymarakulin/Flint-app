package ru.andmar.flint.features.settings.ui

import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.settings.domain.UserDetails

data class SettingsUiState(
    val userDetails: UserDetails = UserDetails(),
    val flintActions: FlintActions = FlintActions.Default
)
