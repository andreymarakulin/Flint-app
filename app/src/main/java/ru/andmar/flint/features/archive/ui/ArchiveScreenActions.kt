package ru.andmar.flint.features.archive.ui

import ru.andmar.flint.features.category.domain.model.CategoryDetails
import ru.andmar.flint.features.label.domain.model.LabelDetails
import ru.andmar.flint.features.note.domain.model.NoteDetails
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails
import ru.andmar.flint.features.todo.domain.model.TodoDetails

sealed interface ArchiveScreenActions {
    data class UnarchiveCategory(val categoryDetails: CategoryDetails): ArchiveScreenActions
    data class UnarchiveNote(val noteDetails: NoteDetails): ArchiveScreenActions
    data class UnarchiveTodo(val todoDetails: TodoDetails): ArchiveScreenActions
    data class UnarchiveLabel(val labelDetails: LabelDetails): ArchiveScreenActions
    data class UnarchiveReminders(val reminderDetails: ReminderDetails): ArchiveScreenActions
}