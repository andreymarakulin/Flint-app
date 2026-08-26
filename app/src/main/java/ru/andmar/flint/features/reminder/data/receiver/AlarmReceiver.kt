package ru.andmar.flint.features.reminder.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.andmar.flint.core.data.DefaultFirestoreClient
import ru.andmar.flint.features.reminder.data.notification.NotificationService

class AlarmReceiver : BroadcastReceiver(), KoinComponent {

    override fun onReceive(context: Context, intent: Intent?) {
        val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: ""
        val reminderId = intent?.getStringExtra("EXTRA_ID")

        val notificationService = NotificationService(context)

        notificationService.showNotification(message)

        if (reminderId == null) return

        val firestoreClient: DefaultFirestoreClient by inject()

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                firestoreClient.updateReminderDoneState(reminderId)
            } catch (e: Exception) {
                notificationService.showNotification(e.message ?: "Не удалось обновить напоминание в Firestore")
            } finally {
                pendingResult.finish()
            }
        }
    }
}