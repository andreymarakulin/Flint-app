package com.andmar.flint.ui.theme.label

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.andmar.flint.FlintActions
import com.andmar.flint.data.DefaultFlintRepository

class LabelViewModel(
    private val flintRepository: DefaultFlintRepository
): ViewModel() {

    var labelUiState by mutableStateOf(LabelUiState())
        private set

    fun onActions(labelActions: LabelActions) {
        when(labelActions) {
            is LabelActions.DismissError -> {
                updateFlintActions(FlintActions.Default)
            }
        }
    }

    private fun updateFlintActions(flintActions: FlintActions) {

    }
}

data class LabelUiState(
    val flintActions: FlintActions = FlintActions.Default
)

sealed interface LabelActions {
    object DismissError: LabelActions
}