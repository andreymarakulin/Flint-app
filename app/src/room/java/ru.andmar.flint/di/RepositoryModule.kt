package ru.andmar.flint.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.andmar.flint.core.data.FlintDao
import ru.andmar.flint.core.data.FlintDatabase
import ru.andmar.flint.features.account.data.repository.AccountRepository
import ru.andmar.flint.features.category.data.repository.CategoryRepository
import ru.andmar.flint.features.label.data.repository.LabelRepository
import ru.andmar.flint.features.note.data.repository.NoteRepository
import ru.andmar.flint.features.reminder.data.repository.ReminderRepository
import ru.andmar.flint.features.todo.data.repository.TodoRepository

val repositoryModule = module {

    single<FlintDao> { FlintDatabase.getDatabase(androidContext()).flintDao() }

    single { AccountRepository(get()) }
    single { CategoryRepository(get()) }
    single { LabelRepository(get()) }
    single { NoteRepository(get()) }
    single { ReminderRepository(get()) }
    single { TodoRepository(get()) }
}