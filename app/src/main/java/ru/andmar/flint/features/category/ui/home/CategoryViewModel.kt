package ru.andmar.flint.features.category.ui.home

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
import ru.andmar.flint.features.category.domain.model.CategoryDetails
import ru.andmar.flint.features.category.domain.usecase.CategoryActionsUseCase
import ru.andmar.flint.features.category.domain.usecase.CategoryUseCase
import ru.andmar.flint.features.category.ui.components.CategoryAction
import ru.andmar.flint.features.note.ui.home.NoteUiAction

class CategoryViewModel(
    private val categoryUseCase: CategoryUseCase,
    private val categoryActionsUseCase: CategoryActionsUseCase
): ViewModel() {

    val categoryDetailsListState: StateFlow<CategoryDetailsListState> =
        categoryUseCase.getCategoryDetailsList().map { categoryDetails ->
            CategoryDetailsListState(
                categoryDetails.filter{ !it.deleted }.sortedWith(
                    compareByDescending<CategoryDetails> { it.fix }
                        .thenByDescending { it.updateTime }
                )
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = CategoryDetailsListState()
        )

    private val _categoryUiState = MutableStateFlow(CategoryUiState())
    val categoryUiState: StateFlow<CategoryUiState> = _categoryUiState

    private val _categoryUiAction = Channel<CategoryUiAction>()
    val categoryUiAction = _categoryUiAction.receiveAsFlow()

    fun onActions(categoryScreenActions: CategoryScreenActions) {
        when(categoryScreenActions) {
            is CategoryScreenActions.UpdateSelectedCategoryDetails -> {
                _categoryUiState.update {
                    it.copy(selectedCategoryDetails = categoryScreenActions.categoryDetails)
                }
            }
            is CategoryScreenActions.CategoryActions -> {
                when(val categoryAction = categoryScreenActions.categoryAction) {
                    is CategoryAction.FixCategory -> {
                        flintActions {
                            categoryActionsUseCase.fixCategory(categoryAction.categoryDetails)
                        }
                    }
                    is CategoryAction.HighlightCategory -> {
                        flintActions {
                            categoryActionsUseCase.highlightCategory(categoryAction.categoryDetails)
                        }
                    }
                    is CategoryAction.EditCategory -> {
                        viewModelScope.launch {
                            _categoryUiAction.send(
                                CategoryUiAction.EditCategory(categoryAction.categoryId)
                            )
                        }
                    }
                    is CategoryAction.DeleteCategory -> {
                        flintActions {
                            //categoryActionsUseCase.updateCategoryDeleteState(categoryAction.categoryDetails)
                            categoryActionsUseCase.deleteCategory(categoryAction.categoryDetails)
                        }
                    }
                    is CategoryAction.RestoreCategory -> {

                    }
                }
            }
            is CategoryScreenActions.DismissError -> {
                _categoryUiState.update {
                    it.copy(flintActions = FlintActions.Default)
                }
            }
        }
    }

    private fun flintActions(action: suspend () -> Result<Unit>) {
        viewModelScope.launch {
            _categoryUiState.update {
                it.copy(flintActions = FlintActions.Loading)
            }
            action().onSuccess {
                _categoryUiState.update {
                    it.copy(flintActions = FlintActions.Success)
                }
            }.onFailure { e ->
                _categoryUiState.update {
                    it.copy(flintActions = FlintActions.Error(e.message ?: "Error"))
                }
            }
        }
    }
}