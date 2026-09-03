package ru.andmar.flint.features.reminder.domain.model

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


fun isReminderAction(reminderDetails: ReminderDetails): Boolean {
    return with(reminderDetails) {
        title.isNotEmpty() //&& date >= System.currentTimeMillis()
    }
}


fun combineDateAndTime(date: Long, hours: Int, minutes: Int): Long {

    val zoneId = ZoneId.systemDefault()
    val localDateTime = Instant.ofEpochMilli(date)
        .atZone(zoneId)
        .toLocalDateTime()

    val combinedDateTime = LocalDateTime.of(
        localDateTime.year,
        localDateTime.month,
        localDateTime.dayOfMonth,
        hours,
        minutes,
        0,
        0
    )

    return combinedDateTime.atZone(zoneId)
        .toInstant()
        .toEpochMilli()
}


fun dateToUiDate(date: Long): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        .withZone(ZoneId.systemDefault())

    return formatter.format(Instant.ofEpochMilli(date))
}

fun hoursAndMinutesToUiTime(hours: Int, minutes: Int): String {
    return String.format(Locale.US,"%02d:%02d", hours, minutes)
}

fun dateToUi(date: Long): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        .withZone(ZoneId.systemDefault())

    return formatter.format(Instant.ofEpochMilli(date))
}