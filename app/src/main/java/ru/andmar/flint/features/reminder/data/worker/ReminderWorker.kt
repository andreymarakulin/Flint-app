package ru.andmar.flint.features.reminder.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent

class ReminderWorker(appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams), KoinComponent {

   // private val firestoreClient: DefaultFirestoreClient by inject()
    //private val flintAlarmScheduler: FlintAlarmScheduler by inject()

    override suspend fun doWork(): Result {
        return try {
            /*
            val deviceName = "${Build.DEVICE} ${Build.MODEL}"
            val reminderItems = firestoreClient.getReminderItems().first()

            reminderItems.forEach { item ->
                if (deviceName !in item.devicesName) {
                    flintAlarmScheduler.scheduler(item.toReminderDetails())
                }
            }

             */
            Result.success()
        } catch (e: Exception) {
            Result.retry()
            Result.failure()
        }
    }
}

