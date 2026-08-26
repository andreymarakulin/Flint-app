package ru.andmar.flint.features.label.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.andmar.flint.features.label.data.repository.LabelRepository

class LabelViewModel(
    private val labelRepository: LabelRepository
): ViewModel() {

    val labelDetailsListState: StateFlow<LabelDetailsListState> =
        labelRepository.getLabels().map { labelDetails ->
            LabelDetailsListState(labelDetails)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LabelDetailsListState()
        )

    var labelUiState by mutableStateOf(LabelUiState())
        private set

    fun onActions(labelScreenActions: LabelScreenActions) {
        when(labelScreenActions) {
            is LabelScreenActions.DismissError -> {

            }
        }
    }
}