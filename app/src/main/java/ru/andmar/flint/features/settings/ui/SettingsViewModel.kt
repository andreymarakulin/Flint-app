package ru.andmar.flint.features.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.andmar.flint.features.settings.domain.usecase.SettingsUseCase

class SettingsViewModel(
    private val settingsUseCase: SettingsUseCase
): ViewModel() {

    private val _settingsUiState = MutableStateFlow(SettingsUiState())
    val settingsUiState: StateFlow<SettingsUiState> = _settingsUiState


    init {
        viewModelScope.launch {
            _settingsUiState.update {
                it.copy(userDetails = settingsUseCase.getUserDetails())
            }
        }
    }
}