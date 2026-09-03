package ru.andmar.flint.features.label.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.andmar.flint.core.data.DefaultFirestoreClient
import ru.andmar.flint.features.label.data.toLabelDetails
import ru.andmar.flint.features.label.data.toLabelItem
import ru.andmar.flint.features.label.domain.model.LabelDetails

class LabelRepository(private val firestoreClient: DefaultFirestoreClient) {

    suspend fun createLabel(labelDetails: LabelDetails) = firestoreClient.setLabelItem(labelDetails.toLabelItem())
    suspend fun editLabel(labelDetails: LabelDetails) = firestoreClient.setLabelItem(labelDetails.toLabelItem())
    suspend fun deleteLabel(labelDetails: LabelDetails) = firestoreClient.deleteLabelItem(labelDetails.id)
    suspend fun getLabelsByIdOnce(labelId: String): LabelDetails = firestoreClient.getLabelItemOnce(labelId).toLabelDetails()
    fun getLabels(): Flow<List<LabelDetails>> =
        firestoreClient.getLabelItems().map { labelItems ->
            labelItems.map { labelItem -> labelItem.toLabelDetails() }
        }
}