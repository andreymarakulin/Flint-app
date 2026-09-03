package ru.andmar.flint.features.category.ui.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.category.domain.model.isCategoryAction
import ru.andmar.flint.features.category.domain.usecase.EditCategoryUseCase
import ru.andmar.flint.navigation.NavigationRoutes

class EditCategoryViewModel(
    private val editCategoryUseCase: EditCategoryUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val categoryId: String = savedStateHandle.toRoute< NavigationRoutes.EditCategoryScreenRoute>().categoryId
    private val _editCategoryUiState = MutableStateFlow(EditCategoryUiState())
    val editCategoryUiState: StateFlow<EditCategoryUiState> = _editCategoryUiState

    init {
        viewModelScope.launch {
            _editCategoryUiState.update {
                it.copy(
                    categoryDetails = editCategoryUseCase.getCategoryByIdOnce(categoryId)
                )
            }
        }
    }

    fun onActions(editCategoryScreenActions: EditCategoryScreenActions) {
        when(editCategoryScreenActions) {
            is EditCategoryScreenActions.UpdateCategoryDetails -> {
                _editCategoryUiState.update {
                    it.copy(
                        categoryDetails = editCategoryScreenActions.categoryDetails,
                        isAction = isCategoryAction(editCategoryScreenActions.categoryDetails)
                    )
                }
            }
            is EditCategoryScreenActions.EditCategory -> {
                flintActions {
                    editCategoryUseCase.editCategory(
                        _editCategoryUiState.value.categoryDetails
                    )
                }
            }
            is EditCategoryScreenActions.DismissError -> {
                _editCategoryUiState.update {
                    it.copy(
                        flintActions = FlintActions.Default
                    )
                }
            }
        }
    }

    private fun flintActions(action: suspend () -> Result<Unit>) {
        viewModelScope.launch {
            _editCategoryUiState.update {
                it.copy(
                    flintActions = FlintActions.Loading
                )
            }
            action().onSuccess {
                _editCategoryUiState.update {
                    it.copy(
                        flintActions = FlintActions.Success
                    )
                }
            }.onFailure { e ->
                _editCategoryUiState.update {
                    it.copy(
                        flintActions = FlintActions.Error(e.message ?: "Error")
                    )
                }
            }
        }
    }
 }