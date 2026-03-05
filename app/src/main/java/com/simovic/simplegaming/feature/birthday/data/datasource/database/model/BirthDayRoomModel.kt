package com.simovic.simplegaming.feature.birthday.data.datasource.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "birthdays")
internal data class BirthDayRoomModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val date: LocalDate,
)
