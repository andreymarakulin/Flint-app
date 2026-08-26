package ru.andmar.flint.features.label.data.repository

import kotlinx.coroutines.flow.Flow
import ru.andmar.flint.core.data.FlintDao
import ru.andmar.flint.features.label.model.LabelItem

class LabelRepository(private val flintDao: FlintDao) {

    fun createLabel(labelItem: LabelItem) {
        TODO("Not yet implemented")
    }

    fun editLabel(labelItem: LabelItem) {
        TODO("Not yet implemented")
    }

    fun getLabels(): Flow<List<LabelItem>> {
        TODO("Not yet implemented")
    }
}