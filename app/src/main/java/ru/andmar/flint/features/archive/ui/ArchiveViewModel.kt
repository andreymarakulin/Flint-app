package ru.andmar.flint.features.archive.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.archive.domain.usecase.ArchiveUseCase
import ru.andmar.flint.features.category.domain.usecase.CategoryUseCase
import ru.andmar.flint.features.label.domain.usecase.LabelUseCase
import ru.andmar.flint.features.note.domain.usecase.NoteUseCase
import ru.andmar.flint.features.reminder.domain.usecase.ReminderUseCase
import ru.andmar.flint.features.todo.domain.usecase.TodoUseCase

class ArchiveViewModel(
    private val categoryUseCase: CategoryUseCase,
    private val noteUseCase: NoteUseCase,
    private val todoUseCase: TodoUseCase,
    private val labelUseCase: LabelUseCase,
    private val reminderUseCase: ReminderUseCase,
    private val archiveUseCase: ArchiveUseCase
): ViewModel() {

    private val _archiveUiState = MutableStateFlow(ArchiveUiState())
    val archiveUiState: StateFlow<ArchiveUiState> = _archiveUiState

    val archiveContentState: StateFlow<ArchiveContentState> = combine(
        categoryUseCase.getCategoryDetailsList(),
        noteUseCase.getNoteDetailsList(),
        todoUseCase.getTodos(),
        labelUseCase.getLabels(),
        reminderUseCase.getReminders()
    ) { categories, notes, todos, labels, reminders ->
        ArchiveContentState(
            categories = categories.filter { it.archive },
            notes = notes.filter { it.archive },
            todos = todos.filter { it.archive },
            labels = labels.filter { it.archive },
            reminders = reminders.filter { it.archive }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = ArchiveContentState()
    )

    fun onActions(archiveScreenActions: ArchiveScreenActions) {
        when(archiveScreenActions) {
            is ArchiveScreenActions.UnarchiveCategory -> {
                flintAction {
                    archiveUseCase.unarchiveCategory(archiveScreenActions.categoryDetails)
                }
            }
            is ArchiveScreenActions.UnarchiveNote -> {
                flintAction {
                    archiveUseCase.unarchiveNote(archiveScreenActions.noteDetails)
                }
            }
            is ArchiveScreenActions.UnarchiveTodo -> {
                flintAction {
                    archiveUseCase.unarchiveTodo(archiveScreenActions.todoDetails)
                }
            }
            is ArchiveScreenActions.UnarchiveLabel -> {
                flintAction {
                    archiveUseCase.unarchiveLabel(archiveScreenActions.labelDetails)
                }
            }
            is ArchiveScreenActions.UnarchiveReminders -> {
                flintAction {
                    archiveUseCase.unarchiveReminder(archiveScreenActions.reminderDetails)
                }
            }
        }
    }

    private fun flintAction(action: suspend () -> Result<Unit>) {
        viewModelScope.launch {
            _archiveUiState.update {
                it.copy(flintActions = FlintActions.Loading)
            }
            action().onSuccess {
                _archiveUiState.update {
                    it.copy(flintActions = FlintActions.Success)
                }
            }.onFailure { e ->
                _archiveUiState.update {
                    it.copy(flintActions = FlintActions.Error(e.message ?: "Error"))
                }
            }
        }
    }
}