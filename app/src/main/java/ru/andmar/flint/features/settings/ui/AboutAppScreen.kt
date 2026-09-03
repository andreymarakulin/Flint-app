package ru.andmar.flint.features.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.andmar.flint.BuildConfig
import ru.andmar.flint.R
import ru.andmar.flint.core.theme.FontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutAppScreen(
    onNavBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "О приложении",
                        fontFamily = FontFamily
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavBack
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = null
                        )
                    }
                }
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                /*
                Image(
                    painter = painterResource(R.drawable.task_alt/*flint_icon*/),
                    contentDescription = null,
                    modifier = Modifier
                        .size(250.dp)
                        .padding(20.dp)
                )

                 */
                Text(
                    text = stringResource(R.string.app_name),
                    modifier = Modifier.padding(vertical = 5.dp)
                )
                Text(
                    text = "Версия: ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
                    modifier = Modifier.padding(vertical = 5.dp)
                )
                Text(
                    text = BuildConfig.FLAVOR,
                    modifier = Modifier.padding(vertical = 5.dp)
                )
                Text(
                    text = "ANDMAR",
                    fontFamily = FontFamily,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                )
                Text(
                    text = "Project",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                )
            }
        }

        item {
            Column() {
                Text(
                    text = "Соцсети",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(10.dp)
                )
                LinkCard(
                    image = R.drawable.task_alt,
                    title = "Сообщество VK"
                ) { uriHandler.openUri("https://vk.ru/andmar_official") }
                LinkCard(
                    image = R.drawable.task_alt,
                    title = "Telegram канал"
                ) { uriHandler.openUri("https://t.me/andmar_official") }
                LinkCard(
                    image = R.drawable.task_alt,
                    title = "Канал MAX"
                ) { uriHandler.openUri("https://max.ru/join/EdjqOnvJRh-dj6MEwEVzdVckY3bR3ragLEjBkI4MGJc") }
            }
        }
        item {
            /*
            Text(
                text = "Соглашение",
                fontSize = 20.sp,
                modifier = Modifier.padding(10.dp)
            )
            Card(
                onClick = {},
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .padding(vertical = 5.dp)
            ) {
                Text(
                    text = "Пользовательское соглашение",

                    modifier = Modifier.padding(10.dp)
                )
            }
            Card(
                onClick = {},
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .padding(vertical = 5.dp)
            ) {
                Text(
                    text = "Политика конфиденциальности",
                    fontFamily = FontFamily,
                    modifier = Modifier.padding(10.dp)
                )
            }

             */
        }
        /*
        item {
            Text(
                text = "Это личный учебный проект, созданный для удобного планирования задач и закрепления навыков современной Android-разработки.Приложение создавалось для личного использования, но открыто для всех желающих! Оно полностью бесплатно, не содержит рекламы и скрытых подписок.",
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(10.dp)
            )
        }

         */
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
            /*
            Image(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .padding(start = 10.dp)
                    .padding(vertical = 10.dp)
            )

             */
            Text(
                text = title,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}