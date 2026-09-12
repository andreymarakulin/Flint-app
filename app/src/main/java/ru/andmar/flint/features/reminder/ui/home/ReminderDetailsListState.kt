package ru.andmar.flint.features.reminder.ui.home

import ru.andmar.flint.features.reminder.domain.model.ReminderDetails

data class ReminderDetailsListState(
    val reminderDetailsList: List<ReminderDetails> = emptyList(),
    val reminderDetailsDoneList: List<ReminderDetails> = emptyList()
)