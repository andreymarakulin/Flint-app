package ru.andmar.flint.features.category.ui.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
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
import ru.andmar.flint.features.category.ui.entry.CategoryDetailsForm

@Composable
fun EditCategoryScreen(
    viewModel: EditCategoryViewModel = koinViewModel(),
    onNavBack: () -> Unit
) {

    val editCategoryUiState = viewModel.editCategoryUiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                navIcon = R.drawable.arrow_back,
                navDes = null,
                onNavIcon = onNavBack
            )
        }
    ) { innerPadding ->
        EditCategoryBody(
            innerPaddingValues = innerPadding,
            editCategoryUiState = editCategoryUiState.value,
            onSuccess = onNavBack
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun EditCategoryBody(
    innerPaddingValues: PaddingValues,
    editCategoryUiState: EditCategoryUiState,
    onSuccess: () -> Unit,
    onActions: (EditCategoryScreenActions) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        DefaultScreenText(stringResource(R.string.edit_category_title))
        CategoryDetailsForm(
            categoryDetails = editCategoryUiState.categoryDetails
        ) { }

        //{ onActions(EditCategoryScreenActions.UpdateCategoryDetails(it)) }
        DefaultButton(
            title = stringResource(R.string.edit_category_button),
            enabled = editCategoryUiState.isAction
        ) { onActions(EditCategoryScreenActions.EditCategory) }
    }

    when(editCategoryUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> onSuccess()
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = editCategoryUiState.flintActions.message
            ) { onActions(EditCategoryScreenActions.DismissError) }
        }
    }
}