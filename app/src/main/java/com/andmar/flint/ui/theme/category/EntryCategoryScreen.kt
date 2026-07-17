package com.andmar.flint.ui.theme.category

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
import com.andmar.flint.ui.theme.label.EntryLabelActions
import com.andmar.flint.ui.theme.label.LabelDetails
import com.andmar.flint.ui.theme.label.LabelDetailsForm
import kotlinx.serialization.Serializable

@Serializable
object EntryCategoryScreenRoute

@Composable
fun EntryCategoryScreen(
    viewModel: EntryCategoryViewModel = viewModel(factory = FlintViewModelProvider.Factory),
    onNavBack: () -> Unit
) {

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = "",
                navIcon = R.drawable.arrow_back,
                navDes = "",
                onNavIcon = onNavBack
            )
        }
    ) { innerPadding ->
        EntryCategoryBody(
            innerPaddingValues = innerPadding,
            entryCategoryUiState = viewModel.entryCategoryUiState,
            onSuccess = onNavBack
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun EntryCategoryBody(
    innerPaddingValues: PaddingValues,
    entryCategoryUiState: EntryCategoryUiState,
    onSuccess: () -> Unit,
    onActions: (EntryCategoryActions) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        DefaultScreenText(stringResource(R.string.entry_category_screen_title))
        CategoryDetailsForm(
            categoryDetails = entryCategoryUiState.categoryDetails
        ) { onActions(EntryCategoryActions.UpdateCategoryDetails(it)) }
        DefaultButton(
            title = stringResource(R.string.continue_button),
            enabled = entryCategoryUiState.isAction
        ) { onActions(EntryCategoryActions.CreateCategory) }
    }

    when(entryCategoryUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> onSuccess()
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = entryCategoryUiState.flintActions.message
            ) { onActions(EntryCategoryActions.DismissError) }
        }
    }
}

@Composable
fun CategoryDetailsForm(
    categoryDetails: CategoryDetails,
    updateCategoryDetails: (CategoryDetails) -> Unit
) {
    Column {
        DefaultTextField(
            value = categoryDetails.title,
            maxLiens = 1,
            label = stringResource(R.string.title_label),
        ) { updateCategoryDetails(categoryDetails.copy(title = it)) }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryActionsSheet(
    sheetState: SheetState,
    categoryDetails: CategoryDetails,
    onFix: () -> Unit,
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
            title = if (categoryDetails.fix) {
                R.string.unfix_note_title
            } else R.string.fix_note_title,
            icon = if (categoryDetails.fix) {
                R.drawable.keep_off
            } else R.drawable.keep,
            desc = null
        ) {
            onFix()
            onDismiss()
        }
        DefaultSheetItem(
            title = if (categoryDetails.highlight) {
                R.string.unhighlight_note_title
            } else R.string.highlight_note_title,
            icon = if (categoryDetails.highlight) {
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