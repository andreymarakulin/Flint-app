package com.andmar.flint.ui.theme.category

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andmar.flint.FlintActions
import com.andmar.flint.data.DefaultFlintRepository
import com.andmar.flint.ui.theme.home.CategoryDetailsState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CategoryViewModel(
    private val flintRepository: DefaultFlintRepository
): ViewModel() {

    val categoryDetailsState: StateFlow<CategoryDetailsState> =
        flintRepository.getCategories().map { categoryItems ->
            CategoryDetailsState(
                categoryItems.sortedByDescending { it.fix }.map { categoryItem ->
                    categoryItem.toCategoryDetails()
                }
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = CategoryDetailsState()
        )

    var categoryUiState by mutableStateOf(CategoryUiState())
        private set

    fun onActions(categoryActions: CategoryActions) {
        when(categoryActions) {
            is CategoryActions.UpdateCategoryDetails -> {
                updateSelectedCategoryDetails(categoryActions.categoryDetails)
            }
            is CategoryActions.FixCategory -> fixCategory()
            is CategoryActions.HighlightCategory -> highlightCategory()
            is CategoryActions.DeleteCategory -> deleteCategory()
            is CategoryActions.DismissError -> {
                updateFlintActions(FlintActions.Default)
            }
        }
    }

    private fun updateSelectedCategoryDetails(categoryDetails: CategoryDetails) {
        categoryUiState = categoryUiState.copy(
            selectedCategoryDetails = categoryDetails
        )
    }

    private fun updateFlintActions(flintActions: FlintActions) {
        categoryUiState = categoryUiState.copy(
            flintActions = flintActions
        )
    }

    private fun fixCategory() {
        val categoryDetails = categoryUiState.selectedCategoryDetails

        flintRepository.editCategory(
            categoryItem = categoryDetails.copy(
                fix = !categoryDetails.fix
            ).toCategoryItem()
        ) { updateFlintActions(it) }
    }

    private fun highlightCategory() {
        val categoryDetails = categoryUiState.selectedCategoryDetails

        flintRepository.editCategory(
            categoryItem = categoryDetails.copy(
                 highlight = !categoryDetails.highlight
            ).toCategoryItem()
        ) { updateFlintActions(it) }
    }

    private fun deleteCategory() {
        flintRepository.deleteCategory(
            categoryItem = categoryUiState.selectedCategoryDetails.toCategoryItem()
        ) { updateFlintActions(it) }
    }
}

data class CategoryUiState(
    val selectedCategoryDetails: CategoryDetails = CategoryDetails(),
    val flintActions: FlintActions = FlintActions.Default
)

sealed interface CategoryActions {
    data class UpdateCategoryDetails(val categoryDetails: CategoryDetails): CategoryActions
    object FixCategory: CategoryActions
    object HighlightCategory: CategoryActions
    object DeleteCategory: CategoryActions
    object DismissError: CategoryActions
}