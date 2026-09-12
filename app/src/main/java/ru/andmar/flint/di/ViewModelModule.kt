package ru.andmar.flint.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.andmar.flint.features.account.ui.home.AccountViewModel
import ru.andmar.flint.features.account.ui.signIn.SignInViewModel
import ru.andmar.flint.features.account.ui.signUp.SignUpViewModel
import ru.andmar.flint.features.archive.ui.ArchiveViewModel
import ru.andmar.flint.features.basket.ui.BasketViewModel
import ru.andmar.flint.features.category.ui.edit.EditCategoryViewModel
import ru.andmar.flint.features.category.ui.entry.EntryCategoryViewModel
import ru.andmar.flint.features.category.ui.home.CategoryViewModel
import ru.andmar.flint.features.label.ui.edit.EditLabelViewModel
import ru.andmar.flint.features.label.ui.entry.EntryLabelViewModel
import ru.andmar.flint.features.label.ui.home.LabelViewModel
import ru.andmar.flint.features.note.ui.details.DetailsViewModel
import ru.andmar.flint.features.note.ui.edit.EditNoteViewModel
import ru.andmar.flint.features.note.ui.entry.EntryNoteViewModel
import ru.andmar.flint.features.note.ui.home.NoteViewModel
import ru.andmar.flint.features.reminder.data.scheduler.AlarmScheduler
import ru.andmar.flint.features.reminder.data.scheduler.FlintAlarmScheduler
import ru.andmar.flint.features.reminder.ui.edit.EditReminderViewModel
import ru.andmar.flint.features.reminder.ui.entry.EntryReminderViewModel
import ru.andmar.flint.features.reminder.ui.home.ReminderViewModel
import ru.andmar.flint.features.settings.ui.SettingsViewModel
import ru.andmar.flint.features.todo.ui.edit.EditTodoViewModel
import ru.andmar.flint.features.todo.ui.entry.EntryTodoViewModel
import ru.andmar.flint.features.todo.ui.home.TodoViewModel
import ru.andmar.flint.ui.main.MainViewModel

val viewModelModule = module {

    single<AlarmScheduler> { FlintAlarmScheduler(androidContext()) }

    viewModelOf(::MainViewModel)
    viewModelOf(::CategoryViewModel)
    viewModelOf(::EntryCategoryViewModel)
    viewModelOf(::EditCategoryViewModel)
    viewModelOf(::NoteViewModel)
    viewModelOf(::EntryNoteViewModel)
    viewModelOf(::EditNoteViewModel)
    viewModelOf(::DetailsViewModel)
    viewModelOf(::TodoViewModel)
    viewModelOf(::EntryTodoViewModel)
    viewModelOf(::EditTodoViewModel)
    viewModelOf(::LabelViewModel)
    viewModelOf(::EntryLabelViewModel)
    viewModelOf(::EditLabelViewModel)
    viewModelOf(::ReminderViewModel)
    viewModelOf(::EntryReminderViewModel)
    viewModelOf(::EditReminderViewModel)
    viewModelOf(::AccountViewModel)
    viewModelOf(::SignInViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::ArchiveViewModel)
    viewModelOf(::BasketViewModel)
}