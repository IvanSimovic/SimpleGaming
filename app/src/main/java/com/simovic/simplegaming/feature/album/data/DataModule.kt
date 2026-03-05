package com.simovic.simplegaming.feature.album.data

import androidx.room.Room
import com.simovic.simplegaming.feature.album.data.datasource.api.service.AlbumRetrofitService
import com.simovic.simplegaming.feature.album.data.datasource.database.AlbumDatabase
import com.simovic.simplegaming.feature.album.data.mapper.AlbumMapper
import com.simovic.simplegaming.feature.album.data.mapper.ImageMapper
import com.simovic.simplegaming.feature.album.data.mapper.ImageSizeMapper
import com.simovic.simplegaming.feature.album.data.mapper.TagMapper
import com.simovic.simplegaming.feature.album.data.mapper.TrackMapper
import com.simovic.simplegaming.feature.album.data.repository.AlbumRepositoryImpl
import com.simovic.simplegaming.feature.album.domain.repository.AlbumRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit

internal val dataModule =
    module {

        singleOf(::AlbumRepositoryImpl) { bind<AlbumRepository>() }

        single { get<Retrofit>().create(AlbumRetrofitService::class.java) }

        single {
            Room
                .databaseBuilder(
                    get(),
                    AlbumDatabase::class.java,
                    "Albums.db",
                ).addMigrations(AlbumDatabase.MIGRATION_1_2)
                .build()
        }

        single { get<AlbumDatabase>().albums() }

        singleOf(::ImageSizeMapper)
        singleOf(::ImageMapper)
        singleOf(::TrackMapper)
        singleOf(::TagMapper)
        singleOf(::AlbumMapper)
    }
