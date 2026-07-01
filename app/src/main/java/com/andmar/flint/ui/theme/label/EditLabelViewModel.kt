package com.andmar.flint.ui.theme.label

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.andmar.flint.FlintActions
import com.andmar.flint.data.DefaultFlintRepository

class EditLabelViewModel(
    private val flintRepository: DefaultFlintRepository
): ViewModel() {

    var editLabelUiState by mutableStateOf(EditLabelUiState())
        private set

    fun onActions(editLabelActions: EditLabelActions) {
        when(editLabelActions) {
            is EditLabelActions.UpdateLabelDetails -> {
                updateLabelDetails(editLabelActions.labelDetails)
            }
            is EditLabelActions.EditLabel -> { editLabel() }
            is EditLabelActions.DismissError -> {
                updateFlintActions(FlintActions.Default)
            }
        }
    }

    private fun updateLabelDetails(labelDetails: LabelDetails) {
        editLabelUiState = EditLabelUiState(
            labelDetails = labelDetails,
            isAction = isLabelAction(labelDetails)
        )
    }

    private fun updateFlintActions(flintActions: FlintActions) {
        editLabelUiState = editLabelUiState.copy(
            flintActions = flintActions
        )
    }

    private fun editLabel() {

    }
}

data class EditLabelUiState(
    val labelDetails: LabelDetails = LabelDetails(),
    val flintActions: FlintActions = FlintActions.Default,
    val isAction: Boolean = false
)

sealed interface EditLabelActions {
    data class UpdateLabelDetails(val labelDetails: LabelDetails): EditLabelActions
    object EditLabel: EditLabelActions
    object DismissError: EditLabelActions
}