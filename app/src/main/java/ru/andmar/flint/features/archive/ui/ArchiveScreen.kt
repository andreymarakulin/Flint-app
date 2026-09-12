package ru.andmar.flint.features.archive.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.components.DefaultTopAppBar
import ru.andmar.flint.features.category.ui.components.cards.ArchiveCategoryDetailsCard
import ru.andmar.flint.features.label.ui.components.cards.ArchiveLabelDetailsCard
import ru.andmar.flint.features.note.ui.components.cards.ArchiveNoteDetailsCard
import ru.andmar.flint.features.reminder.ui.components.cards.ArchiveReminderDetailsCard
import ru.andmar.flint.features.todo.ui.components.cards.ArchiveTodoDetailsCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchiveScreen(
    viewModel: ArchiveViewModel = koinViewModel(),
    onNavBack: () -> Unit
) {

    val archiveContentState = viewModel.archiveContentState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = stringResource(R.string.archive_title),
                navIcon = R.drawable.arrow_back,
                onNavIcon = onNavBack
            )
        }
    ) { contentPadding ->
        ArchiveBody(
            contentPadding = contentPadding,
            contentState = archiveContentState.value
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun ArchiveBody(
    contentPadding: PaddingValues,
    contentState: ArchiveContentState,
    onActions: (ArchiveScreenActions) -> Unit
) {

    val scope = rememberCoroutineScope()
    val tabRowList = listOf("Category", "Notes", "Todos", "Labels", "Reminders")

    val pagerState = rememberPagerState() { tabRowList.size }

    Column(
        modifier = Modifier.fillMaxSize().padding(contentPadding)
    ) {
        PrimaryScrollableTabRow(selectedTabIndex = pagerState.currentPage) {
            tabRowList.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = title
                            )
                        }
                    },
                    modifier = Modifier.clip(RoundedCornerShape(20.dp))
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxSize()
        ) { pager ->
            when(pager) {
                0 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(
                            key = { it.lazyKey },
                            items = contentState.categories
                        ) { categoryDetails ->
                            ArchiveCategoryDetailsCard(
                                modifier = Modifier.animateItem(),
                                categoryDetails = categoryDetails
                            ) { onActions(ArchiveScreenActions.UnarchiveCategory(categoryDetails)) }
                        }
                        item {
                            Box(
                                modifier = Modifier.height(60.dp)
                            )
                        }
                    }
                }
                1 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(
                            key = { it.lazyKey },
                            items = contentState.notes
                        ) { noteDetails ->
                            ArchiveNoteDetailsCard(
                                modifier = Modifier.animateItem(),
                                noteDetails = noteDetails,
                            ) { onActions(ArchiveScreenActions.UnarchiveNote(noteDetails)) }
                        }
                        item {
                            Box(
                                modifier = Modifier.height(60.dp)
                            )
                        }
                    }
                }
                2 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(
                            key = { it.lazyKey },
                            items = contentState.todos
                        ) { todoDetails ->
                            ArchiveTodoDetailsCard(
                                modifier = Modifier.animateItem(),
                                todoDetails = todoDetails
                            ) { onActions(ArchiveScreenActions.UnarchiveTodo(todoDetails)) }
                        }
                        item {
                            Box(
                                modifier = Modifier.height(60.dp)
                            )
                        }
                    }
                }
                3 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(
                            key = { it.lazyKey },
                            items = contentState.labels
                        ) { labelDetails ->
                            ArchiveLabelDetailsCard(
                                modifier = Modifier.animateItem(),
                                labelDetails = labelDetails,
                            ) { onActions(ArchiveScreenActions.UnarchiveLabel(labelDetails)) }
                        }
                        item {
                            Box(
                                modifier = Modifier.height(60.dp)
                            )
                        }
                    }
                }
                4 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(
                            key = { it.lazyKey },
                            items = contentState.reminders
                        ) { reminderDetails ->
                            ArchiveReminderDetailsCard(
                                modifier = Modifier.animateItem(),
                                reminderDetails = reminderDetails
                            ) { onActions(ArchiveScreenActions.UnarchiveReminders(reminderDetails)) }
                        }
                        item {
                            Box(
                                modifier = Modifier.height(60.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}