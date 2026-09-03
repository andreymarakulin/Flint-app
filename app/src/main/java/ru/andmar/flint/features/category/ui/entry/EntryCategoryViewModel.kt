package ru.andmar.flint.features.category.ui.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.category.domain.model.isCategoryAction
import ru.andmar.flint.features.category.domain.usecase.EntryCategoryUseCase

class EntryCategoryViewModel(
    private val entryCategoryUseCase: EntryCategoryUseCase
): ViewModel() {

    private val _entryCategoryUiState = MutableStateFlow(EntryCategoryUiState())
    val entryCategoryUiState: StateFlow<EntryCategoryUiState> = _entryCategoryUiState

    fun onActions(entryCategoryScreenActions: EntryCategoryScreenActions) {
        when(entryCategoryScreenActions) {
            is EntryCategoryScreenActions.UpdateCategoryScreenDetails -> {
                _entryCategoryUiState.update {
                    it.copy(
                        categoryDetails = entryCategoryScreenActions.categoryDetails,
                        isAction = isCategoryAction(entryCategoryScreenActions.categoryDetails)
                    )
                }
            }
            is EntryCategoryScreenActions.CreateCategoryScreen -> {
                flintActions {
                    entryCategoryUseCase.entryCategory(
                        _entryCategoryUiState.value.categoryDetails
                    )
                }
            }
            is EntryCategoryScreenActions.DismissError -> {
                _entryCategoryUiState.update {
                    it.copy(flintActions = FlintActions.Default)
                }
            }
        }
    }

    private fun flintActions(action: suspend () -> Result<Unit>) {
        viewModelScope.launch {
            _entryCategoryUiState.update {
                it.copy(flintActions = FlintActions.Loading)
            }
            action().onSuccess {
                _entryCategoryUiState.update {
                    it.copy(flintActions = FlintActions.Success)
                }
            }.onFailure { e ->
                _entryCategoryUiState.update {
                    it.copy(flintActions = FlintActions.Error(e.message ?: "Error"))
                }
            }
        }
    }
}