package com.andmar.flint.ui.theme.category

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andmar.flint.FlintActions
import com.andmar.flint.FlintRepository
import kotlinx.coroutines.launch
import java.util.UUID

class EntryCategoryViewModel(
    private val flintRepository: FlintRepository
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
        viewModelScope.launch {
            updateFlintActions(FlintActions.Loading)
            try {
                flintRepository.createCategory(entryCategoryUiState.categoryDetails)
                updateFlintActions(FlintActions.Success)
            } catch (e: Exception) {
                updateFlintActions(FlintActions.Error(e.message ?: "Error"))
            }
        }
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