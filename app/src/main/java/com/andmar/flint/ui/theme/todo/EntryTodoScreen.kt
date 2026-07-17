package com.andmar.flint.ui.theme.todo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
data class EntryTodoScreenRoute(val noteId: String)

@Composable
fun EntryTodoScreen(
    viewModel: EntryTodoViewModel = viewModel(factory = FlintViewModelProvider.Factory),
    onNavBack: () -> Unit
) {

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = "",
                navIcon = R.drawable.arrow_back,
                navDes = null,
                onNavIcon = onNavBack
            )
        }
    ) { innerPadding ->
        EntryTodoBody(
            innerPaddingValues = innerPadding,
            entryTodoUiState = viewModel.entryTodoUiState,
            onSuccess = onNavBack
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun EntryTodoBody(
    innerPaddingValues: PaddingValues,
    entryTodoUiState: EntryTodoUiState,
    onSuccess: () -> Unit,
    onActions: (EntryTodoActions) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        DefaultScreenText(stringResource(R.string.entry_todo_screen_title))
        TodoDetailsForm(
            todoDetails = entryTodoUiState.todoDetails,
        ) { onActions(EntryTodoActions.UpdateTodoDetails(it)) }
        DefaultButton(
            title = stringResource(R.string.continue_button),
            enabled = entryTodoUiState.isAction
        ) { onActions(EntryTodoActions.CreateTodo) }
    }

    when(entryTodoUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> onSuccess()
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = entryTodoUiState.flintActions.message
            ) { onActions(EntryTodoActions.DismissError) }
        }
    }
}

@Composable
fun TodoDetailsForm(
    todoDetails: TodoDetails,
    updateTodoDetails: (TodoDetails) -> Unit
) {
    Column {
        DefaultTextField(
            value = todoDetails.title,
            maxLiens = 1,
            label = stringResource(R.string.title_label),
        ) { updateTodoDetails(todoDetails.copy(title = it)) }
        DefaultTextField(
            value = todoDetails.text,
            maxLiens = 6,
            label = stringResource(R.string.text_label),
        ) { updateTodoDetails(todoDetails.copy(text = it)) }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoActionsSheet(
    sheetState: SheetState,
    todoDetails: TodoDetails,
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
            title = if (todoDetails.fix) {
                R.string.unfix_note_title
            } else R.string.fix_note_title,
            icon = if (todoDetails.fix) {
                R.drawable.keep_off
            } else R.drawable.keep,
            desc = null
        ) {
            onFix()
            onDismiss()
        }
        DefaultSheetItem(
            title = if (todoDetails.done) {
                R.string.undone_note_title
            } else R.string.done_note_title,
            icon = if (todoDetails.done) {
                R.drawable.remove_done
            } else R.drawable.done_all,
            desc = null
        ) {
            onDone()
            onDismiss()
        }
        DefaultSheetItem(
            title = if (todoDetails.highlight) {
                R.string.unhighlight_note_title
            } else R.string.highlight_note_title,
            icon = if (todoDetails.highlight) {
                R.drawable.heart_broken
            } else R.drawable.favorite,
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