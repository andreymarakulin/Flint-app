package ru.andmar.flint.features.category.domain.model


fun isCategoryAction(categoryDetails: CategoryDetails): Boolean {
    return categoryDetails.title.isNotBlank()
}