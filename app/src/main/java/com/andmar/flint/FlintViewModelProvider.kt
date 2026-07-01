package com.andmar.flint

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.andmar.flint.ui.theme.account.AccountViewModel
import com.andmar.flint.ui.theme.account.SignInViewModel
import com.andmar.flint.ui.theme.account.SingUpViewModel
import com.andmar.flint.ui.theme.category.CategoryViewModel
import com.andmar.flint.ui.theme.category.EditCategoryViewModel
import com.andmar.flint.ui.theme.category.EntryCategoryViewModel
import com.andmar.flint.ui.theme.details.DetailsViewModel
import com.andmar.flint.ui.theme.home.HomeViewModel
import com.andmar.flint.ui.theme.label.EditLabelViewModel
import com.andmar.flint.ui.theme.label.EntryLabelViewModel
import com.andmar.flint.ui.theme.label.LabelViewModel
import com.andmar.flint.ui.theme.note.EditNoteViewModel
import com.andmar.flint.ui.theme.note.EntryNoteViewModel
import com.andmar.flint.ui.theme.reminder.EditReminderViewModel
import com.andmar.flint.ui.theme.reminder.EntryReminderViewModel
import com.andmar.flint.ui.theme.reminder.ReminderViewModel
import com.andmar.flint.ui.theme.todo.EditTodoViewModel
import com.andmar.flint.ui.theme.todo.EntryTodoViewModel
import com.andmar.flint.ui.theme.todo.TodoViewModel

object FlintViewModelProvider {

    val Factory = viewModelFactory {

        //Home
        initializer {
            HomeViewModel(
                flintRepository = flintApplication().flintRepository
            )
        }
        //Category
        initializer {
            CategoryViewModel(
                flintRepository = flintApplication().flintRepository
            )
        }
        initializer {
            EntryCategoryViewModel(
                flintRepository = flintApplication().flintRepository
            )
        }
        initializer {
            EditCategoryViewModel(
                flintRepository = flintApplication().flintRepository,
                savedStateHandle = createSavedStateHandle()
            )
        }
        //Note
        initializer {
            EntryNoteViewModel(
                flintRepository = flintApplication().flintRepository,
                savedStateHandle = createSavedStateHandle()
            )
        }
        initializer {
            EditNoteViewModel(
                flintRepository = flintApplication().flintRepository,
                savedStateHandle = createSavedStateHandle()
            )
        }
        //Details
        initializer {
            DetailsViewModel(
                flintRepository = flintApplication().flintRepository,
                savedStateHandle = createSavedStateHandle()
            )
        }
        //Todo
        initializer {
            TodoViewModel(
                flintRepository = flintApplication().flintRepository
            )
        }
        initializer {
            EntryTodoViewModel(
                flintRepository = flintApplication().flintRepository,
                savedStateHandle = createSavedStateHandle()
            )
        }
        initializer {
            EditTodoViewModel(
               flintRepository = flintApplication().flintRepository,
                savedStateHandle = createSavedStateHandle()
            )
        }
        //Label
        initializer {
            LabelViewModel(
                flintRepository = flintApplication().flintRepository
            )
        }
        initializer {
            EntryLabelViewModel(
                flintRepository = flintApplication().flintRepository
            )
        }
        initializer {
            EditLabelViewModel(
                flintRepository = flintApplication().flintRepository
            )
        }
        //Reminder
        initializer {
            ReminderViewModel(
                flintRepository = flintApplication().flintRepository
            )
        }
        initializer {
            EntryReminderViewModel(
                flintRepository = flintApplication().flintRepository
            )
        }
        initializer {
            EditReminderViewModel(
                flintRepository = flintApplication().flintRepository
            )
        }
        //Account
        initializer {
            AccountViewModel(
                flintRepository = flintApplication().flintRepository
            )
        }
        initializer {
            SignInViewModel(
                flintRepository = flintApplication().flintRepository
            )
        }
        initializer {
            SingUpViewModel(
                flintRepository = flintApplication().flintRepository
            )
        }
    }
}

fun CreationExtras.flintApplication() =
    (this[APPLICATION_KEY] as FlintApplication)