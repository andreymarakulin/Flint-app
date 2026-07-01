package com.andmar.flint.ui.theme.account

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.serialization.Serializable

@Serializable
object AccountScreenRoute

@Composable
fun AccountScreen(
    viewModel: AccountViewModel = viewModel(),
    onNavBack: () -> Unit
) {

}