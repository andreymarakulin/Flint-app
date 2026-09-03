package ru.andmar.flint.features.label.ui.components

import ru.andmar.flint.features.label.domain.model.LabelDetails
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails

interface LabelAction {

    data class FixReminder(val labelDetails: LabelDetails) : LabelAction
    data class DoneReminder(val labelDetails: LabelDetails) : LabelAction
    data class HighlightReminder(val labelDetails: LabelDetails) : LabelAction
    data class EditReminder(val labelId: String) : LabelAction
    data class DeleteReminder(val labelDetails: LabelDetails) : LabelAction
    data class RestoreReminder(val labelDetails: LabelDetails) : LabelAction
}