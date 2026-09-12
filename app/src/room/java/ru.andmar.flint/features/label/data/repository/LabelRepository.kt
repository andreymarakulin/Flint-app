package ru.andmar.flint.features.label.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.andmar.flint.core.data.FlintDao
import ru.andmar.flint.features.label.data.model.LabelItem
import ru.andmar.flint.features.label.data.toLabelDetails
import ru.andmar.flint.features.label.data.toLabelItem
import ru.andmar.flint.features.label.domain.model.LabelDetails

class LabelRepository(private val flintDao: FlintDao) {

    suspend fun createLabel(labelDetails: LabelDetails) = flintDao.insertLabelItem(labelDetails.toLabelItem())

    suspend fun editLabel(labelDetails: LabelDetails) = flintDao.updateLabelItem(labelDetails.toLabelItem())

    suspend fun deleteLabel(labelDetails: LabelDetails) = flintDao.deleteLabelItem(labelDetails.toLabelItem())
    suspend fun getLabelsByIdOnce(labelId: String): LabelDetails =
        flintDao.getLabelItemById(labelId).first().toLabelDetails()

    fun getLabels(): Flow<List<LabelDetails>> = flintDao.getLabelItems()
        .map { labelItems -> labelItems.map { labelItem -> labelItem.toLabelDetails() } }
}