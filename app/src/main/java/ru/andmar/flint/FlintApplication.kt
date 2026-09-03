package ru.andmar.flint

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.andmar.flint.di.viewModelModule
import ru.andmar.flint.di.repositoryModule
import ru.andmar.flint.features.reminder.data.notification.NotificationService
import ru.andmar.flint.features.reminder.data.worker.ReminderWorker
import java.util.concurrent.TimeUnit

class FlintApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@FlintApplication)
            modules(viewModelModule, repositoryModule)
        }

        if (BuildConfig.FLAVOR == "firebase" && false) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val reminderWorkRequest =
                PeriodicWorkRequestBuilder<ReminderWorker>(15, TimeUnit.MINUTES)
                    .setConstraints(constraints)
                    .setInitialDelay(30, TimeUnit.MINUTES)
                    .build()

            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "ReminderWorker",
                ExistingPeriodicWorkPolicy.KEEP,
                reminderWorkRequest
            )
        }

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NotificationService.REMINDER_CHANNEL_ID,
            "Напоминание",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = "Используется для показа напоминаний"

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }
}