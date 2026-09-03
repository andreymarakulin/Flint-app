package ru.andmar.flint.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.andmar.flint.features.account.domain.usecase.AccountUseCase
import ru.andmar.flint.features.account.domain.usecase.SignInUseCase
import ru.andmar.flint.features.account.domain.usecase.SignUpUseCase
import ru.andmar.flint.features.account.ui.home.AccountViewModel
import ru.andmar.flint.features.account.ui.signIn.SignInViewModel
import ru.andmar.flint.features.account.ui.signUp.SignUpViewModel
import ru.andmar.flint.features.category.domain.usecase.CategoryActionsUseCase
import ru.andmar.flint.features.category.domain.usecase.CategoryUseCase
import ru.andmar.flint.features.category.domain.usecase.EditCategoryUseCase
import ru.andmar.flint.features.category.domain.usecase.EntryCategoryUseCase
import ru.andmar.flint.features.category.ui.edit.EditCategoryViewModel
import ru.andmar.flint.features.category.ui.entry.EntryCategoryViewModel
import ru.andmar.flint.features.category.ui.home.CategoryViewModel
import ru.andmar.flint.features.label.ui.edit.EditLabelViewModel
import ru.andmar.flint.features.label.ui.entry.EntryLabelViewModel
import ru.andmar.flint.features.label.ui.home.LabelViewModel
import ru.andmar.flint.features.note.domain.usecase.DetailsUseCase
import ru.andmar.flint.features.note.domain.usecase.EditNoteUseCase
import ru.andmar.flint.features.note.domain.usecase.EntryNoteUseCase
import ru.andmar.flint.features.note.domain.usecase.NoteActionsUseCase
import ru.andmar.flint.features.note.domain.usecase.NoteUseCase
import ru.andmar.flint.features.note.ui.details.DetailsViewModel
import ru.andmar.flint.features.note.ui.edit.EditNoteViewModel
import ru.andmar.flint.features.note.ui.entry.EntryNoteViewModel
import ru.andmar.flint.features.note.ui.home.NoteViewModel
import ru.andmar.flint.features.reminder.data.scheduler.AlarmScheduler
import ru.andmar.flint.features.reminder.data.scheduler.FlintAlarmScheduler
import ru.andmar.flint.features.reminder.domain.usecase.EditReminderUseCase
import ru.andmar.flint.features.reminder.domain.usecase.EntryReminderUseCase
import ru.andmar.flint.features.reminder.domain.usecase.ReminderActionsUseCase
import ru.andmar.flint.features.reminder.domain.usecase.ReminderUseCase
import ru.andmar.flint.features.reminder.ui.edit.EditReminderViewModel
import ru.andmar.flint.features.reminder.ui.entry.EntryReminderViewModel
import ru.andmar.flint.features.reminder.ui.home.ReminderViewModel
import ru.andmar.flint.features.settings.domain.usecase.SettingsUseCase
import ru.andmar.flint.features.settings.ui.SettingsViewModel
import ru.andmar.flint.features.todo.domain.usecase.EditTodoUseCase
import ru.andmar.flint.features.todo.domain.usecase.EntryTodoUseCase
import ru.andmar.flint.features.todo.domain.usecase.TodoActionsUseCase
import ru.andmar.flint.features.todo.domain.usecase.TodoUseCase
import ru.andmar.flint.features.todo.ui.edit.EditTodoViewModel
import ru.andmar.flint.features.todo.ui.entry.EntryTodoViewModel
import ru.andmar.flint.features.todo.ui.home.TodoViewModel

val viewModelModule = module {

    single<AlarmScheduler> { FlintAlarmScheduler(androidContext()) }

    //Account
    singleOf(::AccountUseCase)
    singleOf(::SignInUseCase)
    singleOf(::SignUpUseCase)
    //Category
    singleOf(::CategoryActionsUseCase)
    singleOf(::CategoryUseCase)
    singleOf(::EditCategoryUseCase)
    singleOf(::EntryCategoryUseCase)
    //Label
    //singleOf(::SignUpUseCase)
    //Note
    singleOf(::DetailsUseCase)
    singleOf(::EditNoteUseCase)
    singleOf(::EntryNoteUseCase)
    singleOf(::NoteActionsUseCase)
    singleOf(::NoteUseCase)
    //Reminder
    singleOf(::EditReminderUseCase)
    singleOf(::EntryReminderUseCase)
    singleOf(::ReminderActionsUseCase)
    singleOf(::ReminderUseCase)
    //Todo
    singleOf(::EditTodoUseCase)
    singleOf(::EntryTodoUseCase)
    singleOf(::TodoActionsUseCase)
    singleOf(::TodoUseCase)

    //Settings
    singleOf(::SettingsUseCase)

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
}