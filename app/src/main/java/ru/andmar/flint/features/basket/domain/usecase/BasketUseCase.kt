package ru.andmar.flint.features.basket.domain.usecase

import ru.andmar.flint.features.category.data.repository.CategoryRepository
import ru.andmar.flint.features.category.domain.model.CategoryDetails
import ru.andmar.flint.features.label.data.repository.LabelRepository
import ru.andmar.flint.features.label.domain.model.LabelDetails
import ru.andmar.flint.features.note.data.repository.NoteRepository
import ru.andmar.flint.features.note.domain.model.NoteDetails
import ru.andmar.flint.features.reminder.data.repository.ReminderRepository
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails
import ru.andmar.flint.features.todo.data.repository.TodoRepository
import ru.andmar.flint.features.todo.domain.model.TodoDetails

class BasketUseCase(
    private val categoryRepository: CategoryRepository,
    private val noteRepository: NoteRepository,
    private val todoRepository: TodoRepository,
    private val labelRepository: LabelRepository,
    private val reminderRepository: ReminderRepository
) {

    suspend fun restoreFromBasketCategory(categoryDetails: CategoryDetails) = runCatching {
        categoryRepository.editCategory(
            categoryDetails.copy(deleted = false)
        )
    }

    suspend fun restoreFromBasketNote(noteDetails: NoteDetails) = runCatching {
        noteRepository.editNote(
            noteDetails.copy(deleted = false)
        )
    }

    suspend fun restoreFromBasketTodo(todoDetails: TodoDetails) = runCatching {
        todoRepository.editTodo(
            todoDetails.copy(deleted = false)
        )
    }

    suspend fun restoreFromBasketLabel(labelDetails: LabelDetails) = runCatching {
        labelRepository.editLabel(
            labelDetails.copy(deleted = false)
        )
    }

    suspend fun restoreFromBasketReminder(reminderDetails: ReminderDetails) = runCatching {
        reminderRepository.editReminder(
            reminderDetails.copy(deleted = false)
        )
    }
}