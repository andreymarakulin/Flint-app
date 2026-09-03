package ru.andmar.flint.features.category.domain.usecase

import ru.andmar.flint.features.category.data.repository.CategoryRepository
import ru.andmar.flint.features.category.domain.model.CategoryDetails
import java.util.UUID

class EntryCategoryUseCase(private val categoryRepository: CategoryRepository) {

    suspend fun entryCategory(categoryDetails: CategoryDetails) = runCatching {
        categoryRepository.createCategory(
            categoryDetails.copy(
                id = UUID.randomUUID().toString(),
                createTime = System.currentTimeMillis()
            )
        )
    }
}