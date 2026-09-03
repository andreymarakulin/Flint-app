package ru.andmar.flint.features.note.ui.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.core.ui.components.DefaultButton
import ru.andmar.flint.core.ui.components.DefaultLoadingDialog
import ru.andmar.flint.core.ui.components.DefaultScreenText
import ru.andmar.flint.core.ui.components.DefaultTopAppBar
import ru.andmar.flint.core.ui.components.ErrorDialog
import ru.andmar.flint.features.note.ui.entry.NoteDetailsForm
import ru.andmar.flint.navigation.NavigationRoutes

@Composable
fun EditNoteScreen(
    viewModel: EditNoteViewModel = koinViewModel(),
    onNavigationRoutes: (NavigationRoutes) -> Unit,
    onNavBack: () -> Unit
) {

    val editNoteUiState = viewModel.editNoteUiState.collectAsStateWithLifecycle()

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
        EditNoteBody(
            innerPaddingValues = innerPadding,
            editNoteUiState = editNoteUiState.value,
            onSuccess = onNavBack
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun EditNoteBody(
    innerPaddingValues: PaddingValues,
    editNoteUiState: EditNoteUiState,
    onSuccess: () -> Unit,
    onActions: (EditNoteScreenActions) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        DefaultScreenText(stringResource(R.string.edit_note_title))
        NoteDetailsForm(
            noteDetails = editNoteUiState.noteDetails
        ) { onActions(EditNoteScreenActions.UpdateNoteScreenDetails(it)) }
        DefaultButton(
            title = stringResource(R.string.edit_note_button),
            enabled = editNoteUiState.isAction
        ) { onActions(EditNoteScreenActions.EditNoteScreen) }
    }

    when(editNoteUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> onSuccess()
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = editNoteUiState.flintActions.message
            ) { onActions(EditNoteScreenActions.DismissError) }
        }
    }
}