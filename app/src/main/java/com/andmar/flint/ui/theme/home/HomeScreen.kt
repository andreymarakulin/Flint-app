package com.andmar.flint.ui.theme.home

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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.andmar.flint.AuthDialog
import com.andmar.flint.DefaultLoadingDialog
import com.andmar.flint.DefaultSheetItem
import com.andmar.flint.DefaultTopAppBar
import com.andmar.flint.ErrorDialog
import com.andmar.flint.FlintActions
import com.andmar.flint.FlintViewModelProvider
import com.andmar.flint.R
import com.andmar.flint.ui.theme.note.NoteActionsSheet
import com.andmar.flint.ui.theme.note.NoteDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object HomeScreenRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(factory = FlintViewModelProvider.Factory),
    homeMenuSheet: SheetState = rememberModalBottomSheetState(),
    scope: CoroutineScope = rememberCoroutineScope(),
    onNavSignIn: () -> Unit,
    onNavDetails: (String) -> Unit,
    onNavCategory: () -> Unit,
    onNavEntryCategory: () -> Unit,
    onNavEntryNote: (String) -> Unit,
    onNavEditNote: (String) -> Unit,
    onNavTodo: () -> Unit,
) {

    val homeContentState = viewModel.homeContentState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = stringResource(R.string.app_name),
                actionsIcon = R.drawable.more_vert,
                actionsDes = null,
                onActions = { scope.launch { homeMenuSheet.show() } }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavEntryNote(homeContentState.value.selectedCategoryDetails.id) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    painter = painterResource(R.drawable.add),
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->
        HomeBody(
            innerPaddingValues = innerPadding,
            homeUiState = viewModel.homeUiState,
            homeContentState = homeContentState.value,
            onClickDetails = { onNavDetails(it) },
            onClickCreateCategory = { onNavEntryCategory() },
            onClickEditNote = { onNavEditNote(it) },
            onClickSignIn = onNavSignIn
        ) { viewModel.onActions(it) }

        if (homeMenuSheet.isVisible) {
            HomeMenuSheet(
                sheetState = homeMenuSheet,
                onCategory = onNavCategory,
                onTodo = onNavTodo
            ) { scope.launch { homeMenuSheet.hide() } }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBody(
    noteActionsSheetState: SheetState = rememberModalBottomSheetState(),
    scope: CoroutineScope = rememberCoroutineScope(),
    innerPaddingValues: PaddingValues,
    homeUiState: HomeUiState,
    homeContentState: HomeContentState,
    onClickDetails: (String) -> Unit,
    onClickCreateCategory: () -> Unit,
    onClickEditNote: (String) -> Unit,
    onClickSignIn: () -> Unit,
    onActions: (HomeActions) -> Unit
) {

    val safeSelectedIndex = homeUiState.selectedTabIndex
        .coerceAtMost(homeContentState.categories.size - 1)
        .coerceAtLeast(0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        PrimaryScrollableTabRow(selectedTabIndex = safeSelectedIndex) {
            homeContentState.categories.forEachIndexed { index, details ->
                Tab(
                    selected = homeUiState.selectedTabIndex == index,
                    onClick = {
                        onActions(HomeActions.UpdateSelectedTanIndex(index))
                    },
                    text = {
                        Text(
                            text = details.title,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
            TextButton(
                onClick = onClickCreateCategory
            ) { Text("Добавить") }
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(homeContentState.filteredNotes) { noteDetails ->
                NoteDetailsCard(
                    modifier = Modifier.animateItem(),
                    noteDetails = noteDetails,
                    onClickCard = { onClickDetails(noteDetails.id) },
                    onClickActions = {
                        onActions(HomeActions.UpdateSelectedNoteDetails(noteDetails))
                        scope.launch { noteActionsSheetState.show() }
                    }
                )
            }
        }
    }

    if (!homeContentState.isAuth) {
        AuthDialog { onClickSignIn() }
    }

    if (homeContentState.isLoading) {
        DefaultLoadingDialog()
    }

    if (noteActionsSheetState.isVisible) {
        NoteActionsSheet(
            sheetState = noteActionsSheetState,
            onFix = { onActions(HomeActions.FixNote) },
            onDone = { onActions(HomeActions.DoneNote) },
            onHighlight = { onActions(HomeActions.HighlightNote) },
            onEdit = { onClickEditNote(homeUiState.selectedNoteDetails.id) },
            onDelete = { onActions(HomeActions.DeleteNote) }
        ) { scope.launch { noteActionsSheetState.hide() } }
    }

    when(homeUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> {}
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = homeUiState.flintActions.message
            ) { onActions(HomeActions.DismissError) }
        }
    }
}

@Composable
fun NoteDetailsCard(
    modifier: Modifier,
    noteDetails: NoteDetails,
    onClickCard: () -> Unit,
    onClickActions: () -> Unit
) {
    Card(
        onClick = onClickCard,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .padding(horizontal = 10.dp)
            .padding(vertical = 5.dp)
            .border(
                width = 3.dp,
                shape = RoundedCornerShape(20.dp),
                color = if (noteDetails.highlight) {
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
            Column {
                if (noteDetails.title.isNotEmpty()) {
                    Text(
                        text = noteDetails.title,
                        fontSize = 16.sp,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (noteDetails.done) {
                            TextDecoration.LineThrough
                        } else TextDecoration.None,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .padding(vertical = 5.dp)
                    )
                }
                if (noteDetails.text.isNotEmpty()) {
                    Text(
                        text = noteDetails.text,
                        fontSize = 13.sp,
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (noteDetails.done) {
                            TextDecoration.LineThrough
                        } else TextDecoration.None,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .padding(bottom = 5.dp)
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (noteDetails.fix) {
                    Icon(
                        painter = painterResource(R.drawable.keep),
                        contentDescription = null,
                        //modifier = Modifier.padding(3.dp)
                    )
                }
                IconButton(
                    onClick = onClickActions
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
fun HomeMenuSheet(
    sheetState: SheetState,
    onCategory: () -> Unit,
    onTodo: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss
    ) {
        DefaultSheetItem(
            title = R.string.category_screen_title,
            icon = R.drawable.category,
            desc = null
        ) {
            onCategory()
            onDismiss()
        }
        DefaultSheetItem(
            title = R.string.todo_screen_title,
            icon = R.drawable.done_all,
            desc = null
        ) {
            onTodo()
            onDismiss()
        }
    }
}