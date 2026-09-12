package ru.andmar.flint.features.label.domain.usecase

import ru.andmar.flint.features.label.data.repository.LabelRepository
import ru.andmar.flint.features.label.domain.model.LabelDetails

class EditLabelUseCase(private val labelRepository: LabelRepository) {

    suspend fun getLabelDetails(labelId: String) = labelRepository.getLabelsByIdOnce(labelId)

    suspend fun editLabel(labelDetails: LabelDetails) = runCatching {
        labelRepository.editLabel(labelDetails)
    }
}