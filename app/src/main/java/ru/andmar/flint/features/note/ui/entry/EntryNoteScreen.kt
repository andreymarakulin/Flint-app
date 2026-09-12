package ru.andmar.flint.features.note.ui.entry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.core.ui.components.button.DefaultButton
import ru.andmar.flint.core.ui.components.dialog.DefaultLoadingDialog
import ru.andmar.flint.core.ui.components.text.DefaultScreenText
import ru.andmar.flint.core.ui.components.DefaultTextField
import ru.andmar.flint.core.ui.components.DefaultTopAppBar
import ru.andmar.flint.core.ui.components.dialog.ErrorDialog
import ru.andmar.flint.features.note.domain.model.NoteDetails

@Composable
fun EntryNoteScreen(
    viewModel: EntryNoteViewModel = koinViewModel(),
    onNavBack: () -> Unit
) {

    val entryNoteUiState = viewModel.entryNoteUiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = "",
                navIcon = R.drawable.arrow_back,
                navDes = "Go to back",
                onNavIcon = onNavBack
            )
        }
    ) { innerPadding ->
        EntryNoteBody(
            innerPaddingValues = innerPadding,
            entryNoteUiState = entryNoteUiState.value,
            onSuccess = onNavBack
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun EntryNoteBody(
    innerPaddingValues: PaddingValues,
    entryNoteUiState: EntryNoteUiState,
    onSuccess: () -> Unit,
    onActions: (EntryNoteScreenActions) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
            .imePadding()
    ) {
        DefaultScreenText(stringResource(R.string.entry_note_title))
        NoteDetailsForm(
            noteDetails = entryNoteUiState.noteDetails
        ) { onActions(EntryNoteScreenActions.UpdateNoteScreenDetails(it)) }
        DefaultButton(
            title = stringResource(R.string.entry_note_button),
            enabled = entryNoteUiState.isAction
        ) { onActions(EntryNoteScreenActions.CreateNoteScreen) }
    }

    when(entryNoteUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> onSuccess()
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = entryNoteUiState.flintActions.message
            ) { onActions(EntryNoteScreenActions.DismissError) }
        }
    }
}

@Composable
fun NoteDetailsForm(
    noteDetails: NoteDetails,
    updateNoteDetails: (NoteDetails) -> Unit
) {
    Column {
        DefaultTextField(
            value = noteDetails.title,
            maxLiens = 1,
            label = stringResource(R.string.title_label),
        ) { updateNoteDetails(noteDetails.copy(title = it)) }
        DefaultTextField(
            value = noteDetails.text,
            maxLiens = 6,
            label = stringResource(R.string.text_label),
        ) { updateNoteDetails(noteDetails.copy(text = it)) }
    }
}