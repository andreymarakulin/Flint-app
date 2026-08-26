package ru.andmar.flint.features.label.ui.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ru.andmar.flint.features.label.data.repository.LabelRepository

class EditLabelViewModel(
    private val labelRepository: LabelRepository
): ViewModel() {

    var editLabelUiState by mutableStateOf(EditLabelUiState())
        private set

    fun onActions(editLabelScreenActions: EditLabelScreenActions) {
        when (editLabelScreenActions) {
            is EditLabelScreenActions.UpdateLabelScreenDetails -> {

            }

            is EditLabelScreenActions.EditLabel -> {

            }

            is EditLabelScreenActions.DismissError -> {

            }
        }
    }
}