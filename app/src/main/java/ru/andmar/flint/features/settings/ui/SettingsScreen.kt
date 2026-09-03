package ru.andmar.flint.features.settings.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.components.DefaultButton
import ru.andmar.flint.core.ui.components.DefaultTopAppBar
import ru.andmar.flint.features.account.components.UserInfoCard
import ru.andmar.flint.features.settings.domain.UserDetails
import ru.andmar.flint.navigation.NavigationRoutes

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    onNavBack: () -> Unit
) {

    val settingsUiState = viewModel.settingsUiState.collectAsStateWithLifecycle()

    SettingsBody(
        settingsUiState = settingsUiState.value
    ) {  }
}

@Composable
fun SettingsBody(
    settingsUiState: SettingsUiState,
    onNavigationRoutes: (NavigationRoutes) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            UserInfoCard(
                userDetails = settingsUiState.userDetails,
                isSettings = true
            ) { onNavigationRoutes(NavigationRoutes.AccountScreenRoute) }
        }
        item {
            ArchiveCard {  }
        }
        item {
            BasketCard {  }
        }
        item {
            Card(
                onClick = { onNavigationRoutes(NavigationRoutes.AboutAppScreenRoute) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = stringResource(R.string.about_app_title),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .padding(vertical = 5.dp)
                )
            }
        }
    }
}


@Composable
fun ArchiveCard(
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = stringResource(R.string.archive_title),
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(vertical = 5.dp)
        )

        Text(
            text = "Пока в разработке",
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(vertical = 5.dp)
        )
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) { Text(stringResource(R.string.archive_manage_button)) }
    }
}


@Composable
fun BasketCard(
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = stringResource(R.string.basket_title),
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(vertical = 5.dp)
        )

        Text(
            text = "Пока в разработке",
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(vertical = 5.dp)
        )
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) { Text(stringResource(R.string.basket_manage_button)) }
    }
}

