package ru.andmar.flint.ui.main

import ru.andmar.flint.core.ui.FlintActions

data class MainUiState(
    val flintActions: FlintActions = FlintActions.Default
)