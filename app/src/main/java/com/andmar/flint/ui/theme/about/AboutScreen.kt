package com.andmar.flint.ui.theme.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andmar.flint.BuildConfig
import com.andmar.flint.DefaultTopAppBar
import com.andmar.flint.R
import com.andmar.flint.ui.theme.FontFamily
import kotlinx.serialization.Serializable

@Serializable
object AboutScreenRoute

@Composable
fun AboutScreen(
    onNavBack: () -> Unit
) {

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                navIcon = R.drawable.arrow_back,
                navDes = "Back",
                onNavIcon = onNavBack
            )
        }
    ) { innerPadding ->
        AboutBody(innerPadding)
    }
}

@Composable
fun AboutBody(innerPaddingValues: PaddingValues) {
    val uriHandler = LocalUriHandler.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        item {
            Text(
                text = "О приложении",
                fontSize = 20.sp,
                fontFamily = FontFamily,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(10.dp)
            )
        }

        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(R.drawable.flint_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(300.dp)
                        .padding(20.dp)
                )
                Text(
                    text = "Версия: ${BuildConfig.VERSION_NAME}",
                    fontFamily = FontFamily,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }

        item {
            Column() {
                LinkCard(
                    image = R.drawable.andmar,
                    title = "Сообщество vk"
                ) { uriHandler.openUri("https://vk.ru/andmar_official") }
                LinkCard(
                    image = R.drawable.vk,
                    title = "Моя страница"
                ) { uriHandler.openUri("https://vk.ru/andrey_marakulin") }
            }
        }
    }
}

@Composable
fun LinkCard(
    image: Int,
    title: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .padding(vertical = 5.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(67.dp)
                    .padding(start = 10.dp)
                    .padding(vertical = 10.dp)
                    .clip(CircleShape)


            )
            Text(
                text = title,
                fontFamily = FontFamily,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}