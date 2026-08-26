package ru.andmar.flint.features.label.ui.entry

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ru.andmar.flint.features.label.data.repository.LabelRepository

class EntryLabelViewModel(
    private val labelRepository: LabelRepository
): ViewModel() {

    var entryLabelUiState by mutableStateOf(EntryLabelUiState())
        private set

    fun onActions(entryLabelScreenActions: EntryLabelScreenActions) {
        when (entryLabelScreenActions) {
            is EntryLabelScreenActions.UpdateLabelScreenDetails -> {

            }
            is EntryLabelScreenActions.CreateLabel -> {}
            is EntryLabelScreenActions.DismissError -> {

            }
        }
    }
}