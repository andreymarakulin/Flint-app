package ru.andmar.flint.features.label.ui.components

import ru.andmar.flint.features.label.domain.model.LabelDetails
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails

interface LabelAction {

    data class FixLabel(val labelDetails: LabelDetails) : LabelAction
    data class DoneLabel(val labelDetails: LabelDetails) : LabelAction
    data class HighlightLabel(val labelDetails: LabelDetails) : LabelAction
    data class ArchiveLabel(val labelDetails: LabelDetails) : LabelAction
    data class ChoiceLabel(val labelDetails: LabelDetails) : LabelAction
    data class EditLabel(val labelId: String) : LabelAction
    data class DeleteLabel(val labelDetails: LabelDetails) : LabelAction
    data class RestoreLabel(val labelDetails: LabelDetails) : LabelAction
}