package ru.andmar.flint.core.ui

sealed interface FlintActions {
    object Default: FlintActions
    object Success: FlintActions
    object Loading: FlintActions
    data class Error(val message: String): FlintActions
}