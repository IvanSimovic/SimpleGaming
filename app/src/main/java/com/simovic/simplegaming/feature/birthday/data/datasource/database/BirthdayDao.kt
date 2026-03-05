package com.simovic.simplegaming.feature.birthday.data.datasource.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.simovic.simplegaming.feature.birthday.data.datasource.database.model.BirthDayRoomModel

@Dao
internal interface BirthdayDao {
    @Query("SELECT * FROM birthdays")
    suspend fun getAll(): List<BirthDayRoomModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(birthday: BirthDayRoomModel)

    @Query("DELETE FROM birthdays WHERE id == :id")
    suspend fun delete(id: Long)
}
