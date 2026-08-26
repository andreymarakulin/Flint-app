package ru.andmar.flint.features.category.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.andmar.flint.core.data.FlintDao
import ru.andmar.flint.features.category.domain.model.CategoryDetails
import ru.andmar.flint.features.category.toCategoryItem
import kotlin.collections.map

class CategoryRepository(private val flintDao: FlintDao) {

    suspend fun createCategory(categoryDetails: CategoryDetails) = flintDao.insertCategoryItem(categoryDetails.toCategoryItem())

    suspend fun editCategory(categoryDetails: CategoryDetails) = flintDao.updateCategoryItem(categoryDetails.toCategoryItem())

    suspend fun deleteCategory(categoryDetails: CategoryDetails) = flintDao.deleteCategoryItem(categoryDetails.toCategoryItem())

    fun getCategoryById(categoryId: String): Flow<CategoryDetails> = flintDao.getCategoryItemById(categoryId)
        .map { categoryItem -> categoryItem.toCategoryDetails() }

    fun getCategories(): Flow<List<CategoryDetails>> = flintDao.getCategoryItems()
        .map { categoryItems ->
            categoryItems.map { categoryItem -> categoryItem.toCategoryDetails() }
        }
}