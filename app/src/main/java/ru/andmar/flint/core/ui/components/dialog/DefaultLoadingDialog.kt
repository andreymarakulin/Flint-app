package ru.andmar.flint.core.ui.components.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun DefaultLoadingDialog() {
    /*
    val infiniteTransition = rememberInfiniteTransition(label = "gradient")
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing)
        ),
        label = "offset"
    )

     */

    Dialog(
        onDismissRequest = {}
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
            /*
           .background(
               brush = Brush.linearGradient(
                   colors = listOf(
                       Color.Gray, // Полупрозрачный синий (Альфа 0x99)
                       Color.Black, // Полупрозрачный фиолетовый
                       Color.White // Полупрозрачный черный для глубины
                   ),
                   start = androidx.compose.ui.geometry.Offset(animatedOffset, animatedOffset),
                   end = androidx.compose.ui.geometry.Offset(animatedOffset + 1000f, animatedOffset + 1000f)
               )
           )

            */
        ) {
            Card() {
                CircularProgressIndicator(
                    modifier = Modifier.padding(20.dp)
                )
            }
        }
    }
}
