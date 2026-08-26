package ru.andmar.flint.features.category.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.andmar.flint.features.category.data.repository.CategoryRepository
import ru.andmar.flint.features.category.domain.model.CategoryDetails
import ru.andmar.flint.features.note.ui.home.ALL_NOTES_CATEGORY_ID
import kotlin.collections.sortedWith

class CategoryUseCase(private val categoryRepository: CategoryRepository) {

    fun getCategoryDetailsList(): Flow<List<CategoryDetails>>
    = categoryRepository.getCategories().map { categoryDetails ->
        categoryDetails.filter{ !it.deleted }.sortedWith(
            compareByDescending<CategoryDetails> { it.fix }
                .thenByDescending { it.updateTime }
        )
    }
}