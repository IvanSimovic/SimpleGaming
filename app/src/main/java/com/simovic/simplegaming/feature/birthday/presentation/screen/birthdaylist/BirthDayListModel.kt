package com.simovic.simplegaming.feature.birthday.presentation.screen.birthdaylist

import com.simovic.simplegaming.base.util.toBirthDay
import com.simovic.simplegaming.feature.birthday.domain.model.BirthDay

data class BirthDayListModel(
    val id: Long,
    val name: String,
    val date: String,
    val isMarkedForDelete: Boolean,
)

fun List<BirthDay>.toBirthDayListModel() = this.map { it.toBirthDayListModel() }

fun BirthDay.toBirthDayListModel() =
    BirthDayListModel(
        id = id,
        name = name,
        date = date.toBirthDay(),
        isMarkedForDelete = false,
    )
