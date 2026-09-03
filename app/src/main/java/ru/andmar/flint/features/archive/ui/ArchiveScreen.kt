package ru.andmar.flint.features.archive.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.components.DefaultTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchiveScreen() {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Archive") },
                navigationIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        ArchiveBody(
            contentPadding = contentPadding
        )
    }
}

@Composable
fun ArchiveBody(
    contentPadding: PaddingValues
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = contentPadding
    ) {
        items(testList) { item ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(10.dp)
            ) {
                Text(
                    text = item,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}

val testList = listOf(
    "kmdkvnwekvnekv",
    "ksdnlvdsnvdjnvjd",
    "njdsvbdjbvdsjbvd",
    "smkdsnvdsnvdvndjv",
    "kmdkvnwekvnekv",
    "ksdnlvdsnvdjnvjd",
    "njdsvbdjbvdsjbvd",
    "smkdsnvdsnvdvndjv",
    "kmdkvnwekvnekv",
    "ksdnlvdsnvdjnvjd",
    "njdsvbdjbvdsjbvd",
    "smkdsnvdsnvdvndjv","kmdkvnwekvnekv",
    "ksdnlvdsnvdjnvjd",
    "njdsvbdjbvdsjbvd",
    "smkdsnvdsnvdvndjv","kmdkvnwekvnekv",
    "ksdnlvdsnvdjnvjd",
    "njdsvbdjbvdsjbvd",
    "smkdsnvdsnvdvndjv","kmdkvnwekvnekv",
    "ksdnlvdsnvdjnvjd",
    "njdsvbdjbvdsjbvd",
    "smkdsnvdsnvdvndjv",
            "kmdkvnwekvnekv",
    "ksdnlvdsnvdjnvjd",
    "njdsvbdjbvdsjbvd",
    "smkdsnvdsnvdvndjv",
)