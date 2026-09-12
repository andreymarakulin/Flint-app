package ru.andmar.flint.features.account.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.andmar.flint.R
import ru.andmar.flint.features.settings.domain.UserDetails


@Composable
fun UserInfoCard(
    userDetails: UserDetails,
    isSettings: Boolean = false,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = stringResource(R.string.account_title),
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(vertical = 5.dp)
        )
        Text(
            text = userDetails.uid,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(vertical = 5.dp)
        )
        Text(
            text = userDetails.email,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(vertical = 5.dp)
        )
        if (isSettings) {
            OutlinedButton(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) { Text(stringResource(R.string.account_manage_button)) }
        }
    }
}
