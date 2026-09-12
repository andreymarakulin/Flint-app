package ru.andmar.flint.features.note.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.logger.PrintLogger
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.category.domain.model.CategoryDetails
import ru.andmar.flint.features.category.domain.usecase.CategoryActionsUseCase
import ru.andmar.flint.features.category.domain.usecase.CategoryUseCase
import ru.andmar.flint.features.category.ui.components.CategoryAction
import ru.andmar.flint.features.label.domain.model.LabelDetails
import ru.andmar.flint.features.label.domain.usecase.LabelUseCase
import ru.andmar.flint.features.label.ui.home.LabelDetailsListState
import ru.andmar.flint.features.note.domain.model.NoteDetails
import ru.andmar.flint.features.note.domain.usecase.NoteActionsUseCase
import ru.andmar.flint.features.note.domain.usecase.NoteUseCase
import ru.andmar.flint.features.note.ui.components.NoteAction

const val ALL_NOTES_CATEGORY_ID = "all_notes"

class NoteViewModel(
    private val categoryUseCase: CategoryUseCase,
    private val noteUseCase: NoteUseCase,
    private val noteActionsUseCase: NoteActionsUseCase,
    private val categoryActionsUseCase: CategoryActionsUseCase,
    private val labelUseCase: LabelUseCase
): ViewModel() {

    private val _noteUiState = MutableStateFlow(NoteUiState())
    val noteUiState: StateFlow<NoteUiState> = _noteUiState
    private val _noteUiAction = Channel<NoteUiAction>()
    val noteUiAction = _noteUiAction.receiveAsFlow()


    val choiceLabelDetailsList: StateFlow<List<LabelDetails>> =
        labelUseCase.getLabels().map { labelDetails ->
            labelDetails.filter { it.choice }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
    val categoryDetailsList: StateFlow<List<CategoryDetails>> =
        categoryUseCase.getCategoryDetailsList().map { noteDetails ->
            noteDetails.sortedWith(
                compareByDescending<CategoryDetails> { it.fix }
                    .thenByDescending { it.updateTime }
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
    val noteDetailsList: StateFlow<List<NoteDetails>> =
        noteUseCase.getNoteDetailsList().map { noteDetails ->
            noteDetails.filter { !it.archive }.sortedWith(
                compareByDescending<NoteDetails> { it.fix }
                    .thenByDescending { it.updateTime }
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
    val noteContentState: StateFlow<NoteContentState> = combine(
        categoryDetailsList,
        noteDetailsList,
        choiceLabelDetailsList
    ) { categories, notes, labels ->

        val defaultCategoryWithCategories = listOf(
            CategoryDetails(id = ALL_NOTES_CATEGORY_ID, title = "Все категории")
        ) + categories

        val filterNotesByLabels = if (labels.isNotEmpty()) {
            val selectedLabelIds = labels.map { it.id }.toSet()
            notes.filter { it.labelDetails.id in selectedLabelIds }
        } else notes

        val notesGroupedByCategory = filterNotesByLabels.groupBy { it.categoryId }

        val filteredNoteDetailsListByCategory = defaultCategoryWithCategories.associate { category ->
            val filteredList = if (category.id == ALL_NOTES_CATEGORY_ID) {
                filterNotesByLabels
            } else {
                notesGroupedByCategory[category.id] ?: emptyList()
            }
            val (done, active) = filteredList.partition { it.done }
            category.id to NoteContentContainer(
                noteDetailsList = active,
                noteDetailsListWhoDone = done
            )
        }

        NoteContentState(
            filteredCategoryDetailsList = defaultCategoryWithCategories,
            filteredNoteDetailsListByCategory = filteredNoteDetailsListByCategory
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NoteContentState()
    )

    val labelDetailsListState: StateFlow<LabelDetailsListState> =
        labelUseCase.getLabels().map { labelDetails ->
            LabelDetailsListState(
                labelDetails.filter { !it.archive }.filter { !it.deleted }.sortedWith(
                    compareByDescending<LabelDetails> { it.fix }
                        .thenByDescending { it.updateTime }
                )
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LabelDetailsListState()
        )

    fun onActions(noteScreenActions: NoteScreenActions) {
        when(noteScreenActions) {
            is NoteScreenActions.UpdateSelectedNoteDetails -> {
                _noteUiState.update {
                    it.copy(selectedNoteDetails = noteScreenActions.noteDetails)
                }
            }
            is NoteScreenActions.UpdateSelectedCategoryDetails -> {
                _noteUiState.update {
                    it.copy(selectedCategoryDetails = noteScreenActions.categoryDetails)
                }
            }
            is NoteScreenActions.NoteActions -> {
                when (val noteAction = noteScreenActions.noteAction) {
                    is NoteAction.FixNote -> {
                        flintAction {
                            noteActionsUseCase.fixNote(noteAction.noteDetails)
                        }
                    }
                    is NoteAction.DoneNote -> {
                        flintAction {
                            noteActionsUseCase.doneNote(noteAction.noteDetails)
                        }
                    }
                    is NoteAction.HighlightNote -> {
                        flintAction {
                            noteActionsUseCase.highlightNote(noteAction.noteDetails)
                        }
                    }
                    is NoteAction.ArchiveNote -> {
                        flintAction {
                            noteActionsUseCase.archiveNote(noteAction.noteDetails)
                        }
                    }
                    is NoteAction.EditLabel -> {
                        viewModelScope.launch {
                            _noteUiAction.send(NoteUiAction.ChoiceLabelSheet)
                        }
                    }
                    is NoteAction.EditNote -> {
                        viewModelScope.launch {
                            _noteUiAction.send(
                                NoteUiAction.EditNote(
                                    noteAction.noteId
                                )
                            )
                        }
                    }
                    is NoteAction.DeleteNote -> {
                        flintAction {
                           // noteActionsUseCase.updateDeleteNoteState(noteAction.noteDetails)
                            noteActionsUseCase.deleteNote(noteAction.noteDetails)
                        }
                        viewModelScope.launch {
                            _noteUiAction.send(NoteUiAction.ShowDeleteSnackbar(noteAction.noteDetails))
                        }
                    }

                    is NoteAction.RestoreNote -> {
                        flintAction {
                            noteActionsUseCase.updateDeleteNoteState(noteAction.noteDetails)
                        }
                    }
                }
            }
            is NoteScreenActions.EditLabel -> {
                flintAction {
                    noteActionsUseCase.editLabel(
                        _noteUiState.value.selectedNoteDetails,
                        noteScreenActions.labelDetails
                    )
                }
            }
            is NoteScreenActions.CategoryActions -> {
                when(val categoryAction = noteScreenActions.categoryAction) {
                    is CategoryAction.FixCategory -> {
                        flintAction {
                            categoryActionsUseCase.fixCategory(categoryAction.categoryDetails)
                        }
                    }
                    is CategoryAction.HighlightCategory -> {
                        flintAction {
                            categoryActionsUseCase.highlightCategory(categoryAction.categoryDetails)
                        }
                    }
                    is CategoryAction.EditCategory -> {

                    }
                    is CategoryAction.DeleteCategory -> {
                        flintAction {
                            //categoryActionsUseCase.updateCategoryDeleteState(categoryAction.categoryDetails)
                            categoryActionsUseCase.deleteCategory(categoryAction.categoryDetails)
                        }
                    }
                    is CategoryAction.RestoreCategory -> {

                    }
                }
            }
            is NoteScreenActions.DismissError -> {
                _noteUiState.update {
                    it.copy(flintActions = FlintActions.Default)
                }
            }
        }
    }

    private fun flintAction(action: suspend () -> Result<Unit>) {
        viewModelScope.launch {
            _noteUiState.update { uiState ->
                uiState.copy(flintActions = FlintActions.Loading)
            }
            action()
                .onSuccess { _noteUiState.update { it.copy(flintActions = FlintActions.Success) } }
                .onFailure { e ->_noteUiState.update { it.copy(flintActions = FlintActions.Error(e.message ?:"Error")) } }
        }
    }
}