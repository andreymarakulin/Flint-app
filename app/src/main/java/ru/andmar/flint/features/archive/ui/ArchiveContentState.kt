package ru.andmar.flint.features.archive.ui

import ru.andmar.flint.features.category.domain.model.CategoryDetails
import ru.andmar.flint.features.label.domain.model.LabelDetails
import ru.andmar.flint.features.note.domain.model.NoteDetails
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails
import ru.andmar.flint.features.todo.domain.model.TodoDetails

data class ArchiveContentState(
    val categories: List<CategoryDetails> = emptyList(),
    val notes: List<NoteDetails> = emptyList(),
    val todos: List<TodoDetails> = emptyList(),
    val labels: List<LabelDetails> = emptyList(),
    val reminders: List<ReminderDetails> = emptyList()
)