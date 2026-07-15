package com.andmar.flint.ui.theme.todo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
data class EditTodoScreenRoute(val todoId: String)

@Composable
fun EditTodoScreen(
    viewModel: EditTodoViewModel = viewModel(factory = FlintViewModelProvider.Factory),
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
        EditTodoBody(
            innerPaddingValues = innerPadding,
            editTodoUiState = viewModel.editTodoUiState,
            onSuccess = onNavBack
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun EditTodoBody(
    innerPaddingValues: PaddingValues,
    editTodoUiState: EditTodoUiState,
    onSuccess: () -> Unit,
    onActions: (EditTodoActions) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        DefaultScreenText(stringResource(R.string.edit_todo_screen_title))
        TodoDetailsForm(
            todoDetails = editTodoUiState.todoDetails,
        ) { onActions(EditTodoActions.UpdateTodoDetails(it)) }
        DefaultButton(
            title = stringResource(R.string.continue_button),
            enabled = editTodoUiState.isAction
        ) { onActions(EditTodoActions.EditTodo) }
    }

    when(editTodoUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> onSuccess()
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = editTodoUiState.flintActions.message
            ) { onActions(EditTodoActions.DismissError) }
        }
    }
}