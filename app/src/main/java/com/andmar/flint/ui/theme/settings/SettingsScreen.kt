package com.andmar.flint.ui.theme.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.andmar.flint.DefaultTopAppBar

@Composable
fun SettingsScreen() {

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = ""
            )
        }
    ) { innerPadding ->
        SettingsBody(
            innerPaddingValues = innerPadding
        )
    }
}

@Composable
fun SettingsBody(
    innerPaddingValues: PaddingValues
) {

}