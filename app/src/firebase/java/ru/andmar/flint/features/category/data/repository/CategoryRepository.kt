package ru.andmar.flint.features.category.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.andmar.flint.core.data.DefaultFirestoreClient
import ru.andmar.flint.features.category.data.toCategoryDetails
import ru.andmar.flint.features.category.data.toCategoryItem
import ru.andmar.flint.features.category.domain.model.CategoryDetails

class CategoryRepository(private val firestoreClient: DefaultFirestoreClient) {

    suspend fun createCategory(categoryDetails: CategoryDetails) = firestoreClient.setCategoryItem(categoryDetails.toCategoryItem())
    suspend fun editCategory(categoryDetails: CategoryDetails) = firestoreClient.setCategoryItem(categoryDetails.toCategoryItem())
    suspend fun deleteCategory(categoryDetails: CategoryDetails) = firestoreClient.deleteCategoryItem(categoryDetails.id)
    suspend fun getCategoryByIdOnce(categoryId: String): CategoryDetails = firestoreClient.getCategoryItemOnce(categoryId).toCategoryDetails()
    fun getCategoryById(categoryId: String): Flow<CategoryDetails> =
        firestoreClient.getCategoryItem(categoryId).map { categoryItem -> categoryItem.toCategoryDetails() }
    fun getCategories(): Flow<List<CategoryDetails>> =
        firestoreClient.getCategoryItems().map { categoryItems ->
            categoryItems.map { categoryItem -> categoryItem.toCategoryDetails() }
        }
}