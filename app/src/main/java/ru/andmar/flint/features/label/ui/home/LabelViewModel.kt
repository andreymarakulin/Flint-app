package ru.andmar.flint.features.label.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.label.data.repository.LabelRepository
import ru.andmar.flint.features.label.domain.model.LabelDetails
import ru.andmar.flint.features.label.domain.usecase.LabelActionsUseCase
import ru.andmar.flint.features.label.ui.components.LabelAction

class LabelViewModel(
    private val labelRepository: LabelRepository,
    private val labelActionsUseCase: LabelActionsUseCase
): ViewModel() {

    val labelDetailsListState: StateFlow<LabelDetailsListState> =
        labelRepository.getLabels().map { labelDetails ->
            LabelDetailsListState(
                labelDetails.filter { !it.archive }.filter { !it.deleted }.sortedWith(
                    compareByDescending<LabelDetails> { it.fix }
                        .thenByDescending { it.updateTime }
                )
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LabelDetailsListState()
        )

    private val _labelUiState = MutableStateFlow(LabelUiState())
    val labelUiState: StateFlow<LabelUiState> = _labelUiState

    private val _labelUiActions = Channel<LabelUiActions>()
    val labelUiActions = _labelUiActions.receiveAsFlow()


    fun onActions(labelScreenActions: LabelScreenActions) {
        when(labelScreenActions) {
            is LabelScreenActions.UpdateSelectedLabelDetails -> {
                _labelUiState.update {
                    it.copy(selectedLabelDetails = labelScreenActions.labelDetails)
                }
            }
            is LabelScreenActions.LabelActions -> {
                when(val labelAction = labelScreenActions.labelAction) {
                    is LabelAction.FixLabel -> {
                        flintAction {
                            labelActionsUseCase.fixLabel(labelAction.labelDetails)
                        }
                    }
                    is LabelAction.DoneLabel -> {
                        flintAction {
                            labelActionsUseCase.doneLabel(labelAction.labelDetails)
                        }
                    }
                    is LabelAction.HighlightLabel -> {
                        flintAction {
                            labelActionsUseCase.highlightLabel(labelAction.labelDetails)
                        }
                    }
                    is LabelAction.ArchiveLabel -> {
                        flintAction {
                            labelActionsUseCase.archiveLabel(labelAction.labelDetails)
                        }
                    }
                    is LabelAction.ChoiceLabel -> {
                        flintAction {
                            labelActionsUseCase.choiceLabel(labelAction.labelDetails)
                        }
                    }
                    is LabelAction.EditLabel -> {
                        viewModelScope.launch {
                            _labelUiActions.send(LabelUiActions.EditLabel(labelAction.labelId))
                        }
                    }
                    is LabelAction.DeleteLabel -> {
                        flintAction {
                            labelActionsUseCase.deleteLabel(labelAction.labelDetails)
                        }
                    }
                }
            }
            is LabelScreenActions.DismissError -> {
                _labelUiState.update {
                    it.copy(flintActions = FlintActions.Default)
                }
            }
        }
    }

    private fun flintAction(action: suspend () -> Result<Unit>) {
        viewModelScope.launch {
            _labelUiState.update {
                it.copy(flintActions = FlintActions.Loading)
            }
            action().onSuccess {
                _labelUiState.update {
                    it.copy(flintActions = FlintActions.Success)
                }
            }.onFailure { e ->
                _labelUiState.update {
                    it.copy(flintActions = FlintActions.Error(e.message ?: "Error"))
                }
            }
        }
    }
}