package com.andmar.flint.ui.theme.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andmar.flint.FlintActions
import com.andmar.flint.FlintRepository
import com.andmar.flint.ui.theme.category.CategoryDetails
import com.andmar.flint.ui.theme.note.NoteDetails
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val flintRepository: FlintRepository
): ViewModel() {

    val authState: StateFlow<AuthState> =
        flintRepository.getAuthState().map { actions ->
            AuthState(actions)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = AuthState()
        )

    val categoryDetailsState: StateFlow<CategoryDetailsState> =
        flintRepository.getCategories().map { categoryItems ->
            CategoryDetailsState(
                categoryItems.sortedByDescending { it.fix }
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = CategoryDetailsState()
        )

    val noteDetailsState: StateFlow<NoteDetailsState> =
        flintRepository.getNotes().map { noteItems ->
            Log.i("tst", noteItems.toString())
            NoteDetailsState(
                noteItems.sortedByDescending { it.fix }
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = NoteDetailsState()
        )

    var homeUiState by mutableStateOf(HomeUiState())
        private set

    val homeContentState: StateFlow<HomeContentState> = combine(
        authState,
        categoryDetailsState,
        noteDetailsState,
        snapshotFlow { homeUiState.selectedTabIndex },
    ) { authState, categoryDetailsState, noteDetailsState, selectedTabIndex ->

        if (authState.flintActions is FlintActions.Success) {

            val categories = categoryDetailsState.categoryDetailsList
            val notes = noteDetailsState.noteDetailsList

            if (categories.isEmpty() || selectedTabIndex !in categories.indices) {
                HomeContentState(
                    filteredNotes = emptyList(),
                    categories = categories,
                    authStateActions = AuthStateActions.Authorized
                )
            } else {
                val currentCategory = categories[selectedTabIndex]
                val targetCategoryId = categories[selectedTabIndex].id
                val filtered = notes.filter { it.categoryId == targetCategoryId }

                HomeContentState(
                    filteredNotes = filtered,
                    categories = categories,
                    selectedCategoryDetails = currentCategory,
                    authStateActions = AuthStateActions.Authorized,
                    isLoading = false
                )
            }
        } else {
            HomeContentState(
                filteredNotes = emptyList(),
                categories = emptyList(),
                authStateActions = AuthStateActions.Unauthorized,
                isLoading = false
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeContentState()
    )

    fun onActions(homeActions: HomeActions) {
        when(homeActions) {
            is HomeActions.UpdateSelectedTanIndex -> {
                updateSelectedTabIndex(homeActions.selectedTabIndex)
            }
            is HomeActions.UpdateSelectedNoteDetails -> {
                updateNoteDetails(homeActions.noteDetails)
            }
            is HomeActions.FixNote -> fixNote()
            is HomeActions.DoneNote -> doneNote()
            is HomeActions.HighlightNote -> highlightNote()
            is HomeActions.DeleteNote -> deleteNote()
            is HomeActions.DismissError -> {
                updateFlintActions(FlintActions.Default)
            }
        }
    }

    private fun updateSelectedTabIndex(selectedTabIndex: Int) {
        homeUiState = homeUiState.copy(
            selectedTabIndex = selectedTabIndex
        )
    }

    private fun updateNoteDetails(noteDetails: NoteDetails) {
        homeUiState = homeUiState.copy(
            selectedNoteDetails = noteDetails
        )
    }

    private fun updateFlintActions(flintActions: FlintActions) {
        homeUiState = homeUiState.copy(
            flintActions = flintActions
        )
    }

    private fun fixNote() {
        val noteDetails = homeUiState.selectedNoteDetails

        viewModelScope.launch {
            try {
                updateFlintActions(FlintActions.Loading)
                flintRepository.editNote(
                    noteDetails.copy(fix = !noteDetails.fix)
                )
                updateFlintActions(FlintActions.Success)
            } catch (e: Exception) {
                updateFlintActions(FlintActions.Error(e.message ?: "Error"))
            }
        }
    }

    private fun doneNote() {
        val noteDetails = homeUiState.selectedNoteDetails

        viewModelScope.launch {
            try {
                updateFlintActions(FlintActions.Loading)
                flintRepository.editNote(
                    noteDetails.copy(done = !noteDetails.done)
                )
                updateFlintActions(FlintActions.Success)
            } catch (e: Exception) {
                updateFlintActions(FlintActions.Error(e.message ?: "Error"))
            }
        }
    }

    private fun highlightNote() {
        val noteDetails = homeUiState.selectedNoteDetails

        viewModelScope.launch {
            try {
                updateFlintActions(FlintActions.Loading)
                flintRepository.editNote(
                    noteDetails.copy(highlight = !noteDetails.highlight)
                )
                updateFlintActions(FlintActions.Success)
            } catch (e: Exception) {
                updateFlintActions(FlintActions.Error(e.message ?: "Error"))
            }
        }
    }

    private fun deleteNote() {
        viewModelScope.launch {
            updateFlintActions(FlintActions.Loading)
            try {
                flintRepository.deleteNote(homeUiState.selectedNoteDetails)
                updateFlintActions(FlintActions.Success)
            } catch (e: Exception) {
                updateFlintActions(FlintActions.Error(e.message ?: "Error"))
            }
        }
    }
}

data class AuthState(val flintActions: FlintActions = FlintActions.Error("No auth"))
data class NoteDetailsState(val noteDetailsList: List<NoteDetails> = emptyList())
data class CategoryDetailsState(val categoryDetailsList: List<CategoryDetails> = emptyList())

data class HomeUiState(
    val selectedTabIndex: Int = 0,
    val selectedNoteDetails: NoteDetails = NoteDetails(),
    val flintActions: FlintActions = FlintActions.Default
)

data class HomeContentState(
    val filteredNotes: List<NoteDetails> = emptyList(),
    val categories: List<CategoryDetails> = emptyList(),
    val selectedCategoryDetails: CategoryDetails = CategoryDetails(),
    val authStateActions: AuthStateActions = AuthStateActions.Loading,
    val isLoading: Boolean = true
)

sealed interface AuthStateActions {
    object Loading : AuthStateActions
    object Authorized : AuthStateActions
    object Unauthorized : AuthStateActions
}

sealed interface HomeActions {
    data class UpdateSelectedTanIndex(val selectedTabIndex: Int) : HomeActions
    data class UpdateSelectedNoteDetails(val noteDetails: NoteDetails) : HomeActions
    object FixNote : HomeActions
    object DoneNote : HomeActions
    object HighlightNote : HomeActions
    object DeleteNote : HomeActions

    object DismissError : HomeActions
}