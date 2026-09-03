package ru.andmar.flint.features.settings.domain.usecase

import ru.andmar.flint.features.account.data.repository.AccountRepository
import ru.andmar.flint.features.settings.domain.UserDetails

class SettingsUseCase(private val accountRepository: AccountRepository) {

    suspend fun getUserDetails(): UserDetails = accountRepository.getUser()
}