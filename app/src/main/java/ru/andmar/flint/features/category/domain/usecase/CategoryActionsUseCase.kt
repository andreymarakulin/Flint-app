package ru.andmar.flint.features.category.domain.usecase

import ru.andmar.flint.core.ui.components.DefaultDetails
import ru.andmar.flint.features.category.data.repository.CategoryRepository
import ru.andmar.flint.features.category.domain.model.CategoryDetails
import ru.andmar.flint.features.todo.domain.model.TodoDetails

class CategoryActionsUseCase(private val categoryRepository: CategoryRepository) {


    suspend fun fixCategory(categoryDetails: CategoryDetails) = runCatching {
        categoryRepository.editCategory(
            categoryDetails.copy(fix = !categoryDetails.fix)
        )
    }

    suspend fun highlightCategory(categoryDetails: CategoryDetails) = runCatching {
        categoryRepository.editCategory(
            categoryDetails.copy(highlight = !categoryDetails.highlight)
        )
    }

    suspend fun updateCategoryDeleteState(categoryDetails: CategoryDetails) = runCatching {
        categoryRepository.editCategory(
            categoryDetails.copy(deleted = !categoryDetails.deleted)
        )
    }

    suspend fun deleteCategory(categoryDetails: CategoryDetails) = runCatching {
        categoryRepository.deleteCategory(categoryDetails)
    }
}