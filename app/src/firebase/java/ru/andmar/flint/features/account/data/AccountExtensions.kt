package ru.andmar.flint.features.account.data

import ru.andmar.flint.features.account.data.model.AuthItem
import ru.andmar.flint.features.account.data.model.UserItem
import ru.andmar.flint.features.account.domain.model.AuthDetails
import ru.andmar.flint.features.settings.domain.UserDetails


fun AuthDetails.toAuthItem(): AuthItem = AuthItem(
    email = email,
    password = password
)

fun AuthItem.toAuthDetails(): AuthDetails = AuthDetails(
    email = email,
    password = password
)

fun UserDetails.toUserItem(): UserItem = UserItem(
    uid = uid,
    email = email
)

fun UserItem.toUserDetails(): UserDetails = UserDetails(
    uid = uid,
    email = email
)