package com.simovic.simplegaming.base.util

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

class DateConverter {
    @TypeConverter
    fun fromEpoch(value: Long?): LocalDate? =
        value?.let {
            Instant
                .ofEpochMilli(it)
                .atZone(ZoneOffset.UTC)
                .toLocalDate()
        }

    @TypeConverter
    fun toEpoch(date: LocalDate?): Long? =
        date
            ?.atStartOfDay(ZoneOffset.UTC)
            ?.toInstant()
            ?.toEpochMilli()
}
