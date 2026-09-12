package ru.andmar.flint.features.label.domain.usecase

import ru.andmar.flint.features.label.data.repository.LabelRepository
import ru.andmar.flint.features.label.domain.model.LabelDetails
import java.util.UUID

class EntryLabelUseCase(private val labelRepository: LabelRepository) {

    suspend fun entryLabel(labelDetails: LabelDetails) = runCatching {
        labelRepository.createLabel(
            labelDetails.copy(
                id = UUID.randomUUID().toString()
            )
        )
    }
}