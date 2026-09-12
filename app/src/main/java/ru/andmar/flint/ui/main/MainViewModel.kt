package ru.andmar.flint.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.label.domain.usecase.LabelActionsUseCase
import ru.andmar.flint.features.label.domain.usecase.LabelUseCase
import ru.andmar.flint.features.label.ui.home.LabelDetailsListState

class MainViewModel(
    private val labelUseCase: LabelUseCase,
    private val labelActionsUseCase: LabelActionsUseCase
): ViewModel() {

    val choiceLabelDetailsListState: StateFlow<LabelDetailsListState> =
        labelUseCase.getLabels().map { labelDetails ->
            LabelDetailsListState(
                labelDetailsList = labelDetails.filter { it.choice }
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = LabelDetailsListState()
        )

    private val _mainUiState = MutableStateFlow(MainUiState())
    val mainUiState: StateFlow<MainUiState> = _mainUiState

    fun onActions(mainScreenActions: MainScreenActions) {
        when(mainScreenActions) {
            is MainScreenActions.RemoveChoiceLabelDetails -> {
                flintActions {
                    labelActionsUseCase.choiceLabel(mainScreenActions.labelDetails)
                }
            }
        }
    }

    private fun flintActions(action: suspend () -> Result<Unit>) {
        viewModelScope.launch {
            _mainUiState.update {
                it.copy(flintActions = FlintActions.Loading)
            }
            action().onSuccess {
                _mainUiState.update {
                    it.copy(flintActions = FlintActions.Success)
                }
            }.onFailure { e ->
                _mainUiState.update {
                    it.copy(flintActions = FlintActions.Error(e.message ?: "Error"))
                }
            }
        }
    }
}