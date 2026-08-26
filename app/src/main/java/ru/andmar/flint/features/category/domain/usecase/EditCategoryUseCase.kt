package ru.andmar.flint.features.category.domain.usecase

import ru.andmar.flint.features.category.data.repository.CategoryRepository
import ru.andmar.flint.features.category.domain.model.CategoryDetails

class EditCategoryUseCase(
    private val categoryRepository: CategoryRepository
) {

    suspend fun getCategoryByIdOnce(categoryId: String): CategoryDetails {
        return categoryRepository.getCategoryByIdOnce(categoryId)
    }

    suspend fun editCategory(categoryDetails: CategoryDetails) = runCatching {
        categoryRepository.editCategory(categoryDetails)
    }
}