package ru.andmar.flint.features.label.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.andmar.flint.features.label.data.repository.LabelRepository
import ru.andmar.flint.features.label.domain.model.LabelDetails

class LabelUseCase(private val labelRepository: LabelRepository) {

    fun getLabels(): Flow<List<LabelDetails>> = labelRepository.getLabels()
}