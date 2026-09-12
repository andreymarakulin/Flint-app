package ru.andmar.flint.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.andmar.flint.features.account.domain.usecase.AccountUseCase
import ru.andmar.flint.features.account.domain.usecase.SignInUseCase
import ru.andmar.flint.features.account.domain.usecase.SignUpUseCase
import ru.andmar.flint.features.archive.domain.usecase.ArchiveUseCase
import ru.andmar.flint.features.basket.domain.usecase.BasketUseCase
import ru.andmar.flint.features.category.domain.usecase.CategoryActionsUseCase
import ru.andmar.flint.features.category.domain.usecase.CategoryUseCase
import ru.andmar.flint.features.category.domain.usecase.EditCategoryUseCase
import ru.andmar.flint.features.category.domain.usecase.EntryCategoryUseCase
import ru.andmar.flint.features.label.domain.usecase.EditLabelUseCase
import ru.andmar.flint.features.label.domain.usecase.EntryLabelUseCase
import ru.andmar.flint.features.label.domain.usecase.LabelActionsUseCase
import ru.andmar.flint.features.label.domain.usecase.LabelUseCase
import ru.andmar.flint.features.note.domain.usecase.DetailsUseCase
import ru.andmar.flint.features.note.domain.usecase.EditNoteUseCase
import ru.andmar.flint.features.note.domain.usecase.EntryNoteUseCase
import ru.andmar.flint.features.note.domain.usecase.NoteActionsUseCase
import ru.andmar.flint.features.note.domain.usecase.NoteUseCase
import ru.andmar.flint.features.reminder.domain.usecase.EditReminderUseCase
import ru.andmar.flint.features.reminder.domain.usecase.EntryReminderUseCase
import ru.andmar.flint.features.reminder.domain.usecase.ReminderActionsUseCase
import ru.andmar.flint.features.reminder.domain.usecase.ReminderUseCase
import ru.andmar.flint.features.settings.domain.usecase.SettingsUseCase
import ru.andmar.flint.features.todo.domain.usecase.EditTodoUseCase
import ru.andmar.flint.features.todo.domain.usecase.EntryTodoUseCase
import ru.andmar.flint.features.todo.domain.usecase.TodoActionsUseCase
import ru.andmar.flint.features.todo.domain.usecase.TodoUseCase

val useCaseModule = module {

    singleOf(::AccountUseCase)
    singleOf(::SignInUseCase)
    singleOf(::SignUpUseCase)
    //Category
    singleOf(::CategoryActionsUseCase)
    singleOf(::CategoryUseCase)
    singleOf(::EditCategoryUseCase)
    singleOf(::EntryCategoryUseCase)
    //Label
    singleOf(::LabelActionsUseCase)
    singleOf(::LabelUseCase)
    singleOf(::EntryLabelUseCase)
    singleOf(::EditLabelUseCase)
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

    //Archive
    singleOf(::ArchiveUseCase)

    //Basket
    singleOf(::BasketUseCase)

}