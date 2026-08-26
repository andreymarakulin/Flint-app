package ru.andmar.flint.features.account.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.ModalSheetItem
import ru.andmar.flint.core.ui.components.DefaultModalSheetItem
import ru.andmar.flint.core.ui.components.DefaultTopAppBar
import ru.andmar.flint.features.account.components.UserInfoCard
import ru.andmar.flint.features.settings.domain.UserDetails
import ru.andmar.flint.features.settings.ui.components.AccountAction
import kotlin.collections.forEach

@Composable
fun AccountScreen(
    viewModel: AccountViewModel = koinViewModel(),
    onNavBack: () -> Unit
) {

    val accountUiState = viewModel.accountUiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = stringResource(R.string.account_title),
                navIcon = R.drawable.arrow_back,
                navDes = null,
                onNavIcon = onNavBack
            )
        }
    ) { contentPadding ->
        AccountBody(
            contentPaddingValues = contentPadding,
            accountUiState = accountUiState.value
        )
    }
}

@Composable
fun AccountBody(
    contentPaddingValues: PaddingValues,
    accountUiState: AccountUiState
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPaddingValues)
    ) {
        item {
            UserInfoCard(accountUiState.userDetails)
        }
        item {
            AccountActions(accountUiState.userDetails)
        }
    }
}

@Composable
fun AccountActions(
    userDetails: UserDetails
) {
    Column() {
        Text(
            text = "Actions",
            modifier = Modifier.padding(10.dp)
        )
        accountActionsItems(userDetails) {

        }.forEach { item ->
            DefaultModalSheetItem(
                title = item.title,
                icon = item.icon,
                description = item.description
            ) { item.onClick() }
        }
    }
}

fun accountActionsItems(
    userDetails: UserDetails,
    onActions: (AccountAction) -> Unit
): List<ModalSheetItem> = listOf(
    ModalSheetItem(
        title = R.string.edit_email_title,
        icon = R.drawable.edit,
        description = null
    ) { onActions(AccountAction.EditEmail(userDetails)) },
    ModalSheetItem(
        title = R.string.edit_password_title,
        icon = R.drawable.edit,
        description = null
    ) { onActions(AccountAction.EditPassword(userDetails)) },
    ModalSheetItem(
        title = R.string.sign_out_title,
        icon = R.drawable.edit,
        description = null
    ) {onActions(AccountAction.SignOut(userDetails)) },
    ModalSheetItem(
        title = R.string.delete_account_title,
        icon = R.drawable.edit,
        description = null
    ) { onActions(AccountAction.DeleteAccount(userDetails)) }
)