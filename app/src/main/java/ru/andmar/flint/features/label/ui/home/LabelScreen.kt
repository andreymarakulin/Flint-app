package ru.andmar.flint.features.label.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.core.ui.components.DefaultLoadingDialog
import ru.andmar.flint.core.ui.components.DefaultTopAppBar
import ru.andmar.flint.core.ui.components.ErrorDialog
import ru.andmar.flint.features.label.domain.model.LabelDetails

@Composable
fun LabelScreen(
    viewModel: LabelViewModel = koinViewModel(),
    onNavBack: () -> Unit
) {

    val labelDetailsListState = viewModel.labelDetailsListState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = ""
            )
        }
    ) { innerPadding ->
        LabelBody(
            innerPaddingValues = innerPadding,
            labelDetailsListState = labelDetailsListState.value,
            labelUiState = viewModel.labelUiState
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun LabelBody(
    innerPaddingValues: PaddingValues,
    labelDetailsListState: LabelDetailsListState,
    labelUiState: LabelUiState,
    onActions: (LabelScreenActions) -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPaddingValues)
    ) {
        items(labelDetailsListState.labelDetailsList) { labelDetails ->
            LabelDetailsCard(
                modifier = Modifier.animateItem(),
                labelDetails = labelDetails
            ) {
                /*
                onActions(HomeActions.UpdateSelectedNoteDetails(noteDetails))
                scope.launch { noteActionsSheetState.show() }

                 */
            }
        }
    }

    when(labelUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> {}
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = labelUiState.flintActions.message
            ) { onActions(LabelScreenActions.DismissError) }
        }
    }
}


@Composable
fun LabelDetailsCard(
    modifier: Modifier,
    labelDetails: LabelDetails,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .padding(horizontal = 10.dp)
            .padding(vertical = 5.dp)
        /*
        .border(
            width = 3.dp,
            shape = RoundedCornerShape(20.dp),
            color = if (noteDetails.highlight) {
                MaterialTheme.colorScheme.primary
            } else Color.Transparent
        )

         */
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(Modifier.weight(1f)) {
                if (labelDetails.title.isNotEmpty()) {
                    Text(
                        text = labelDetails.title,
                        fontSize = 16.sp,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .padding(vertical = 5.dp)
                    )
                }

                //Тут будет отображаться время
                /*
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

                 */
            }
            Row(verticalAlignment = Alignment.CenterVertically) {

                /*
                if (noteDetails.fix) {
                    Icon(
                        painter = painterResource(R.drawable.keep),
                        contentDescription = null,
                        //modifier = Modifier.padding(3.dp)
                    )
                }

                 */
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
