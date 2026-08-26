package ru.andmar.flint.features.account.domain.usecase

import ru.andmar.flint.features.account.data.repository.AccountRepository
import ru.andmar.flint.features.settings.domain.UserDetails

class AccountUseCase(private val accountRepository: AccountRepository) {

    suspend fun getUserDetails(): UserDetails = accountRepository.getUserDetails()
}