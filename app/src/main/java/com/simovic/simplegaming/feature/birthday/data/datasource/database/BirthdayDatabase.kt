package com.simovic.simplegaming.feature.birthday.data.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.simovic.simplegaming.base.util.DateConverter
import com.simovic.simplegaming.feature.birthday.data.datasource.database.model.BirthDayRoomModel

@TypeConverters(DateConverter::class)
@Database(entities = [BirthDayRoomModel::class], version = 1, exportSchema = false)
internal abstract class BirthdayDatabase : RoomDatabase() {
    abstract fun birthDays(): BirthdayDao
}
