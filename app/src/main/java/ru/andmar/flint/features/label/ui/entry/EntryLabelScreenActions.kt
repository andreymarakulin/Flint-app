package ru.andmar.flint.features.label.ui.entry

import ru.andmar.flint.features.label.domain.model.LabelDetails

sealed interface EntryLabelScreenActions {
    data class UpdateLabelScreenDetails(val labelDetails: LabelDetails): EntryLabelScreenActions
    object CreateLabel: EntryLabelScreenActions
    object DismissError: EntryLabelScreenActions
}