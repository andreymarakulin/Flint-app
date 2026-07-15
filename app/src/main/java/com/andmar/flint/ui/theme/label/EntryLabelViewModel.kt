package com.andmar.flint.ui.theme.label

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.andmar.flint.FlintActions
import com.andmar.flint.FlintRepository

class EntryLabelViewModel(
    private val flintRepository: FlintRepository
): ViewModel() {

    var entryLabelUiState by mutableStateOf(EntryLabelUiState())
        private set

    fun onActions(entryLabelActions: EntryLabelActions) {
        when(entryLabelActions) {
            is EntryLabelActions.UpdateLabelDetails -> {

            }
            is EntryLabelActions.CreateLabel -> {}
            is EntryLabelActions.DismissError -> {
                updateFlintActions(FlintActions.Default)
            }
        }
    }

    private fun updateLabelDetails(labelDetails: LabelDetails) {
        entryLabelUiState = EntryLabelUiState(
            labelDetails = labelDetails,
            isAction = isLabelAction(labelDetails)
        )
    }

    private fun updateFlintActions(flintActions: FlintActions) {
        entryLabelUiState = entryLabelUiState.copy(
            flintActions = flintActions
        )
    }
}

data class EntryLabelUiState(
    val labelDetails: LabelDetails = LabelDetails(),
    val flintActions: FlintActions = FlintActions.Default,
    val isAction: Boolean = false
)

sealed interface EntryLabelActions {
    data class UpdateLabelDetails(val labelDetails: LabelDetails): EntryLabelActions
    object CreateLabel: EntryLabelActions
    object DismissError: EntryLabelActions
}
data class LabelDetails(
    val id: String = "",
    val title: String = ""
)

fun isLabelAction(labelDetails: LabelDetails): Boolean {
    return labelDetails.title.isNotBlank()
}