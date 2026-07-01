package com.andmar.flint.ui.theme.category

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.andmar.flint.FlintActions
import com.andmar.flint.data.DefaultFlintRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditCategoryViewModel(
    private val flintRepository: DefaultFlintRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val categoryId: String = savedStateHandle.toRoute<EditCategoryScreenRoute>().categoryId
    var editCategoryUiState by mutableStateOf(EditCategoryUiState())
        private set

    init {
        viewModelScope.launch {
            editCategoryUiState = EditCategoryUiState(
                categoryDetails = flintRepository.getCategoryById(categoryId)
                    .first()
                    .toCategoryDetails()
            )
        }
    }

    fun onActions(editCategoryActions: EditCategoryActions) {
        when(editCategoryActions) {
            is EditCategoryActions.UpdateCategoryDetails -> {
                updateCategoryDetails(editCategoryActions.categoryDetails)
            }
            is EditCategoryActions.EditCategory -> { editCategory() }
            is EditCategoryActions.DismissError -> {
                updateFlintActions(FlintActions.Default)
            }
        }
    }

    private fun updateCategoryDetails(categoryDetails: CategoryDetails) {
        editCategoryUiState = EditCategoryUiState(
            categoryDetails = categoryDetails,
            isAction = isCategoryAction(categoryDetails)
        )
    }

    private fun updateFlintActions(flintActions: FlintActions) {
        editCategoryUiState = editCategoryUiState.copy(
            flintActions = flintActions
        )
    }

    private fun editCategory() {
        flintRepository.editCategory(
            categoryItem = editCategoryUiState.categoryDetails.toCategoryItem()
        ) { updateFlintActions(it) }
    }
}

data class EditCategoryUiState(
    val categoryDetails: CategoryDetails = CategoryDetails(),
    val flintActions: FlintActions = FlintActions.Default,
    val isAction: Boolean = false
)

sealed interface EditCategoryActions {
    data class UpdateCategoryDetails(val categoryDetails: CategoryDetails): EditCategoryActions
    object EditCategory: EditCategoryActions
    object DismissError: EditCategoryActions
}