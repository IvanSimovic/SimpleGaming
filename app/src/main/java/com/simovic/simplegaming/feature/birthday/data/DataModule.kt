package com.simovic.simplegaming.feature.birthday.data

import androidx.room.Room
import com.simovic.simplegaming.feature.birthday.data.datasource.database.BirthdayDatabase
import com.simovic.simplegaming.feature.birthday.data.mapper.BirthdayMapper
import com.simovic.simplegaming.feature.birthday.data.repository.BirthDayRepoImpl
import com.simovic.simplegaming.feature.birthday.domain.repository.BirthDayRepo
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val dataModule =
    module {

        singleOf(::BirthDayRepoImpl) { bind<BirthDayRepo>() }

        single {
            Room
                .databaseBuilder(
                    get(),
                    BirthdayDatabase::class.java,
                    "BirthDay.db",
                ).build()
        }

        single { get<BirthdayDatabase>().birthDays() }

        singleOf(::BirthdayMapper)
    }
