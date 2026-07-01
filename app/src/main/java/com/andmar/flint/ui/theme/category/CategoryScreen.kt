package com.andmar.flint.ui.theme.category

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.andmar.flint.DefaultLoadingDialog
import com.andmar.flint.DefaultSheetItem
import com.andmar.flint.DefaultTopAppBar
import com.andmar.flint.ErrorDialog
import com.andmar.flint.FlintActions
import com.andmar.flint.FlintViewModelProvider
import com.andmar.flint.R
import com.andmar.flint.ui.theme.home.CategoryDetailsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object CategoryScreenRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel = viewModel(factory = FlintViewModelProvider.Factory),
    onNavEditCategory: (String) -> Unit,
    onNavBack: () -> Unit
) {

    val categoryDetailsState = viewModel.categoryDetailsState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = stringResource(R.string.category_screen_title),
                navIcon = R.drawable.arrow_back,
                navDes = null,
                onNavIcon = onNavBack
            )
        }
    ) { innerPadding ->
        CategoryBody(
            innerPaddingValues = innerPadding,
            categoryDetailsState = categoryDetailsState.value,
            categoryUiState = viewModel.categoryUiState,
            onClickEditCategory = { onNavEditCategory(it) }
        ) { viewModel.onActions(it) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryBody(
    categoryActionsSheetState: SheetState = rememberModalBottomSheetState(),
    scope: CoroutineScope = rememberCoroutineScope(),
    innerPaddingValues: PaddingValues,
    categoryDetailsState: CategoryDetailsState,
    categoryUiState: CategoryUiState,
    onClickEditCategory: (String) -> Unit,
    onActions: (CategoryActions) -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        items(categoryDetailsState.categoryDetailsList) { categoryDetails ->
            CategoryDetailsCard(
                modifier = Modifier.animateItem(),
                categoryDetails = categoryDetails
            ) {
                onActions(CategoryActions.UpdateCategoryDetails(categoryDetails))
                scope.launch { categoryActionsSheetState.show() }
            }
        }
    }

    if (categoryActionsSheetState.isVisible) {
        CategoryActionsSheet(
            sheetState = categoryActionsSheetState,
            onFix = { onActions(CategoryActions.FixCategory) },
            onHighlight = { onActions(CategoryActions.HighlightCategory) },
            onEdit = { onClickEditCategory(categoryUiState.selectedCategoryDetails.id) },
            onDelete = { onActions(CategoryActions.DeleteCategory) }
        ) { scope.launch { categoryActionsSheetState.hide() } }
    }

    when(categoryUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> {}
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = categoryUiState.flintActions.message
            ) { onActions(CategoryActions.DismissError) }
        }
    }
}

@Composable
fun CategoryDetailsCard(
    modifier: Modifier,
    categoryDetails: CategoryDetails,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .padding(horizontal = 10.dp)
            .padding(vertical = 5.dp)
            .border(
                width = 3.dp,
                shape = RoundedCornerShape(20.dp),
                color = if (categoryDetails.highlight) {
                    MaterialTheme.colorScheme.primary
                } else Color.Transparent
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = categoryDetails.title,
                fontSize = 16.sp,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .padding(vertical = 5.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (categoryDetails.fix) {
                    Icon(
                        painter = painterResource(R.drawable.keep),
                        contentDescription = null,
                        //modifier = Modifier.padding(3.dp)
                    )
                }
                IconButton(
                    onClick = onClick
                ) {
                    Icon(
                        painter = painterResource(R.drawable.more_vert),
                        contentDescription = null,
                        modifier = Modifier.padding(3.dp)
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryActionsSheet(
    sheetState: SheetState,
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
            title = R.string.fix_note_title,
            icon = R.drawable.keep,
            desc = null
        ) {
            onFix()
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