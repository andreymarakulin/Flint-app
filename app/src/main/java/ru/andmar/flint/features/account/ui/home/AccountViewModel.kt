package ru.andmar.flint.features.account.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.andmar.flint.features.account.data.repository.AccountRepository
import ru.andmar.flint.features.account.domain.usecase.AccountUseCase

class AccountViewModel(
    private val accountUseCase: AccountUseCase
): ViewModel() {

    private val _accountUiState = MutableStateFlow(AccountUiState())
    val accountUiState: StateFlow<AccountUiState> = _accountUiState

    init {
        viewModelScope.launch {
            _accountUiState.update {
                it.copy(userDetails = accountUseCase.getUserDetails())
            }
        }
    }

    fun onActions(accountScreenActions: AccountScreenActions) {
        when(accountScreenActions) {
            is AccountScreenActions.EditEmail -> {

            }
            is AccountScreenActions.EditPassword -> {

            }
            is AccountScreenActions.SignOut -> {

            }
            is AccountScreenActions.DeleteAccount -> {

            }
        }
    }
}