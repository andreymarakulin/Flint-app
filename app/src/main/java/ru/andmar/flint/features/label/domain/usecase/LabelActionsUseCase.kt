package ru.andmar.flint.features.label.domain.usecase

import ru.andmar.flint.features.label.data.repository.LabelRepository
import ru.andmar.flint.features.label.domain.model.LabelDetails

class LabelActionsUseCase(private val labelRepository: LabelRepository) {
    suspend fun updateDeleteLabelState(labelDetails: LabelDetails) = runCatching {
        labelRepository.editLabel(
            labelDetails.copy(deleted = !labelDetails.deleted)
        )
    }
    suspend fun fixLabel(labelDetails: LabelDetails) = runCatching {
        labelRepository.editLabel(
            labelDetails.copy(fix = !labelDetails.fix)
        )
    }

    suspend fun doneLabel(labelDetails: LabelDetails) = runCatching {
        labelRepository.editLabel(
            labelDetails.copy(done = !labelDetails.done)
        )
    }

    suspend fun highlightLabel(labelDetails: LabelDetails) = runCatching {
        labelRepository.editLabel(
            labelDetails.copy(highlight = !labelDetails.highlight)
        )
    }
    suspend fun archiveLabel(labelDetails: LabelDetails) = runCatching {
        labelRepository.editLabel(
            labelDetails.copy(archive = true)
        )
    }

    suspend fun choiceLabel(labelDetails: LabelDetails) = runCatching {
        labelRepository.editLabel(
            labelDetails.copy(choice = !labelDetails.choice)
        )
    }

    suspend fun deleteLabel(labelDetails: LabelDetails) = runCatching() {
        labelRepository.deleteLabel(labelDetails)
    }
}