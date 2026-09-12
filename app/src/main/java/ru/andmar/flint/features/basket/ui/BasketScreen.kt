package ru.andmar.flint.features.basket.ui

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.components.DefaultTopAppBar
import ru.andmar.flint.features.category.ui.components.cards.BasketCategoryDetailsCard
import ru.andmar.flint.features.label.ui.components.cards.BasketLabelDetailsCard
import ru.andmar.flint.features.note.ui.components.cards.BasketNoteDetailsCard
import ru.andmar.flint.features.reminder.ui.components.cards.BasketReminderDetailsCard
import ru.andmar.flint.features.todo.ui.components.cards.BasketTodoDetailsCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasketScreen(
    viewModel: BasketViewModel = koinViewModel(),
    onNavBack: () -> Unit
) {

    val basketContentState = viewModel.basketContentState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = stringResource(R.string.basket_title),
                navIcon = R.drawable.arrow_back,
                onNavIcon = onNavBack
            )
        }
    ) { contentPadding ->
        BasketBody(
            contentPadding = contentPadding,
            contentState = basketContentState.value
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun BasketBody(
    contentPadding: PaddingValues,
    contentState: BasketContentState,
    onActions: (BasketScreenActions) -> Unit
) {

    val scope = rememberCoroutineScope()
    val tabRowList = listOf("Category", "Notes", "Todos", "Labels", "Reminders")

    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

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
                            BasketCategoryDetailsCard(
                                modifier = Modifier.animateItem(),
                                categoryDetails = categoryDetails
                            ) { onActions(BasketScreenActions.RestoreFromBasketCategory(categoryDetails)) }
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
                            BasketNoteDetailsCard(
                                modifier = Modifier.animateItem(),
                                noteDetails = noteDetails,
                            ) { onActions(BasketScreenActions.RestoreFromBasketNote(noteDetails)) }
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
                            BasketTodoDetailsCard(
                                modifier = Modifier.animateItem(),
                                todoDetails = todoDetails
                            ) { onActions(BasketScreenActions.RestoreFromBasketTodo(todoDetails)) }
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
                            BasketLabelDetailsCard(
                                modifier = Modifier.animateItem(),
                                labelDetails = labelDetails,
                            ) { onActions(BasketScreenActions.RestoreFromBasketLabel(labelDetails)) }
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
                            BasketReminderDetailsCard(
                                modifier = Modifier.animateItem(),
                                reminderDetails = reminderDetails
                            ) { onActions(BasketScreenActions.RestoreFromBasketReminders(reminderDetails)) }
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