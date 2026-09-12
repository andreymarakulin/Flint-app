package ru.andmar.flint.features.account.domain.usecase

import ru.andmar.flint.features.account.data.repository.AccountRepository

class EditPasswordUseCase(private val accountRepository: AccountRepository) {

    suspend fun editPassword(password: String) = runCatching {
        accountRepository.updatePassword(password)
    }
}