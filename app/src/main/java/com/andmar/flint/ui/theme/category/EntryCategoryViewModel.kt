package com.andmar.flint.ui.theme.category

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.andmar.flint.FlintActions
import com.andmar.flint.data.DefaultFlintRepository
import com.andmar.flint.firebase.CategoryItem

class EntryCategoryViewModel(
    private val flintRepository: DefaultFlintRepository
): ViewModel() {

    var entryCategoryUiState by mutableStateOf(EntryCategoryUiState())
        private set

    fun onActions(entryCategoryActions: EntryCategoryActions) {
        when(entryCategoryActions) {
            is EntryCategoryActions.UpdateCategoryDetails -> {
                updateCategoryDetails(entryCategoryActions.categoryDetails)
            }
            is EntryCategoryActions.CreateCategory -> { createCategory() }
            is EntryCategoryActions.DismissError -> {
                updateFlintActions(FlintActions.Default)
            }
        }
    }

    private fun updateCategoryDetails(categoryDetails: CategoryDetails) {
        entryCategoryUiState = EntryCategoryUiState(
            categoryDetails = categoryDetails,
            isAction = isCategoryAction(categoryDetails)
        )
    }

    private fun updateFlintActions(flintActions: FlintActions) {
        entryCategoryUiState = entryCategoryUiState.copy(
            flintActions = flintActions
        )
    }

    private fun createCategory() {
        flintRepository.createCategory(
            categoryItem = entryCategoryUiState.categoryDetails.toCategoryItem()
        ) {}
    }
}

data class EntryCategoryUiState(
    val categoryDetails: CategoryDetails = CategoryDetails(),
    val flintActions: FlintActions = FlintActions.Default,
    val isAction: Boolean = false
)

sealed interface EntryCategoryActions {
    data class UpdateCategoryDetails(val categoryDetails: CategoryDetails): EntryCategoryActions
    object CreateCategory: EntryCategoryActions
    object DismissError: EntryCategoryActions
}

data class CategoryDetails(
    val id: String = "",
    val title: String = "",
    val fix: Boolean = false,
    val highlight: Boolean = false,
    val updateTime: Long = 0
)

fun isCategoryAction(categoryDetails: CategoryDetails): Boolean {
    return categoryDetails.title.isNotBlank()
}

fun CategoryItem.toCategoryDetails(): CategoryDetails = CategoryDetails(
    id = id,
    title = title,
    fix = fix,
    highlight = highlight,
    updateTime = updateTime
)


fun CategoryDetails.toCategoryItem(): CategoryItem = CategoryItem(
    id = id,
    title = title,
    fix = fix,
    highlight = highlight,
    updateTime = System.currentTimeMillis()
)