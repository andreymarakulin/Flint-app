package com.andmar.flint.ui.theme.note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.andmar.flint.DefaultButton
import com.andmar.flint.DefaultLoadingDialog
import com.andmar.flint.DefaultScreenText
import com.andmar.flint.DefaultSheetItem
import com.andmar.flint.DefaultTextField
import com.andmar.flint.DefaultTopAppBar
import com.andmar.flint.ErrorDialog
import com.andmar.flint.FlintActions
import com.andmar.flint.FlintViewModelProvider
import com.andmar.flint.R
import kotlinx.serialization.Serializable

@Serializable
data class EntryNoteScreenRoute(val categoryId: String)

@Composable
fun EntryNoteScreen(
    viewModel: EntryNoteViewModel = viewModel(factory = FlintViewModelProvider.Factory),
    onNavBack: () -> Unit
) {

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
            entryNoteUiState = viewModel.entryNoteUiState,
            onSuccess = onNavBack
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun EntryNoteBody(
    innerPaddingValues: PaddingValues,
    entryNoteUiState: EntryNoteUiState,
    onSuccess: () -> Unit,
    onActions: (EntryNoteActions) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
            .imePadding()
    ) {
        DefaultScreenText(stringResource(R.string.entry_note_screen_title))
        NoteDetailsForm(
            noteDetails = entryNoteUiState.noteDetails
        ) { onActions(EntryNoteActions.UpdateNoteDetails(it)) }
        DefaultButton(
            title = stringResource(R.string.continue_button),
            enabled = entryNoteUiState.isAction
        ) { onActions(EntryNoteActions.CreateNote) }
    }

    when(entryNoteUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> onSuccess()
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = entryNoteUiState.flintActions.message
            ) { onActions(EntryNoteActions.DismissError) }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteActionsSheet(
    sheetState: SheetState,
    onFix: () -> Unit,
    onDone: () -> Unit,
    onHighlight: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { onDismiss() }
    ) {
        DefaultSheetItem(
            title = R.string.fix_note_title,
            icon = R.drawable.keep,
            desc = null
        ) {
            onFix()
            onDismiss()
        }
        DefaultSheetItem(
            title = R.string.done_note_title,
            icon = R.drawable.done_outline,
            desc = null
        ) {
            onDone()
            onDismiss()
        }
        DefaultSheetItem(
            title = R.string.highlight_note_title,
            icon = R.drawable.favorite,
            desc = null
        ) {
            onHighlight()
            onDismiss()
        }
        DefaultSheetItem(
            title = R.string.edit_note_title,
            icon = R.drawable.edit,
            desc = null
        ) {
            onEdit()
            onDismiss()
        }
        DefaultSheetItem(
            title = R.string.delete_note_title,
            icon = R.drawable.delete,
            desc = null
        ) {
            onDelete()
            onDismiss()
        }
    }
}