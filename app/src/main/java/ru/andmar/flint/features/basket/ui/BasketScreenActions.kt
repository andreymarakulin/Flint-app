package ru.andmar.flint.features.basket.ui

import ru.andmar.flint.features.archive.ui.ArchiveScreenActions
import ru.andmar.flint.features.category.domain.model.CategoryDetails
import ru.andmar.flint.features.label.domain.model.LabelDetails
import ru.andmar.flint.features.note.domain.model.NoteDetails
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails
import ru.andmar.flint.features.todo.domain.model.TodoDetails

sealed interface BasketScreenActions {
    data class RestoreFromBasketCategory(val categoryDetails: CategoryDetails): BasketScreenActions
    data class RestoreFromBasketNote(val noteDetails: NoteDetails): BasketScreenActions
    data class RestoreFromBasketTodo(val todoDetails: TodoDetails): BasketScreenActions
    data class RestoreFromBasketLabel(val labelDetails: LabelDetails): BasketScreenActions
    data class RestoreFromBasketReminders(val reminderDetails: ReminderDetails): BasketScreenActions
}