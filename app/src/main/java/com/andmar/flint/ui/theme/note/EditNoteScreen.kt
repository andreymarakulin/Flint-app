package com.andmar.flint.ui.theme.note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.andmar.flint.DefaultButton
import com.andmar.flint.DefaultLoadingDialog
import com.andmar.flint.DefaultScreenText
import com.andmar.flint.DefaultTopAppBar
import com.andmar.flint.ErrorDialog
import com.andmar.flint.FlintActions
import com.andmar.flint.FlintViewModelProvider
import com.andmar.flint.R
import kotlinx.serialization.Serializable

@Serializable
data class EditNoteScreenRoute(val noteId: String)

@Composable
fun EditNoteScreen(
    viewModel: EditNoteViewModel = viewModel(factory = FlintViewModelProvider.Factory),
    onNavBack: () -> Unit
) {

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = stringResource(R.string.edit_screen_title),
                navIcon = R.drawable.arrow_back,
                navDes = "Go to back",
                onNavIcon = onNavBack
            )
        }
    ) { innerPadding ->
        EditNoteBody(
            innerPaddingValues = innerPadding,
            editNoteUiState = viewModel.editNoteUiState,
            onSuccess = onNavBack
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun EditNoteBody(
    innerPaddingValues: PaddingValues,
    editNoteUiState: EditNoteUiState,
    onSuccess: () -> Unit,
    onActions: (EditNoteActions) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        DefaultScreenText(stringResource(R.string.edit_note_screen_title))
        NoteDetailsForm(
            noteDetails = editNoteUiState.noteDetails
        ) { onActions(EditNoteActions.UpdateNoteDetails(it)) }
        DefaultButton(
            title = stringResource(R.string.continue_button),
            enabled = editNoteUiState.isAction
        ) { onActions(EditNoteActions.EditNote) }
    }

    when(editNoteUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> onSuccess()
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = editNoteUiState.flintActions.message
            ) { onActions(EditNoteActions.DismissError) }
        }
    }
}