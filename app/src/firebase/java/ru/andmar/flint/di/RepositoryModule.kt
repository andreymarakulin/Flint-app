package ru.andmar.flint.di

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.andmar.flint.core.data.AuthClient
import ru.andmar.flint.core.data.DefaultAuthClient
import ru.andmar.flint.core.data.DefaultFirestoreClient
import ru.andmar.flint.core.data.FirestoreClient
import ru.andmar.flint.features.account.data.repository.AccountRepository
import ru.andmar.flint.features.category.data.repository.CategoryRepository
import ru.andmar.flint.features.label.data.repository.LabelRepository
import ru.andmar.flint.features.note.data.repository.NoteRepository
import ru.andmar.flint.features.reminder.data.repository.ReminderRepository
import ru.andmar.flint.features.reminder.data.scheduler.AlarmScheduler
import ru.andmar.flint.features.reminder.data.scheduler.FlintAlarmScheduler
import ru.andmar.flint.features.todo.data.repository.TodoRepository

val repositoryModule = module {
    single { Firebase.auth }
    single { Firebase.firestore }

    single<DefaultAuthClient> { AuthClient(get()) }
    single<DefaultFirestoreClient> { FirestoreClient(get(), get()) }

    singleOf(::AccountRepository)
    singleOf(::CategoryRepository)
    singleOf(::LabelRepository)
    singleOf(::NoteRepository)
    singleOf(::ReminderRepository)
    singleOf(::TodoRepository)
}