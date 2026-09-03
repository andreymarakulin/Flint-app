package ru.andmar.flint.features.reminder.data.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import ru.andmar.flint.features.reminder.data.receiver.AlarmReceiver
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails

class FlintAlarmScheduler(private val context: Context): AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun scheduler(item: ReminderDetails) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXTRA_MESSAGE", item.title)
            putExtra("EXTRA_ID", item.id)
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            item.reminderDate,
            PendingIntent.getBroadcast(
                context,
                item.reminderId,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel(item: ReminderDetails) {
        alarmManager.cancel {
            PendingIntent.getBroadcast(
                context,
                item.reminderId,
                Intent(context, AlarmManager::class.java),
                PendingIntent.FLAG_IMMUTABLE
            )
        }
    }
}