package ru.andmar.flint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.andmar.flint.core.theme.FlintTheme
import ru.andmar.flint.ui.FlintApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlintTheme {
                FlintApp()
            }
        }
    }
}