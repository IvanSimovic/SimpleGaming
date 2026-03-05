package com.simovic.simplegaming.feature.album.data.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.simovic.simplegaming.feature.album.data.datasource.database.model.AlbumRoomModel
import com.simovic.simplegaming.feature.album.data.datasource.database.model.FavoriteAlbumRoomModel

@Database(entities = [AlbumRoomModel::class, FavoriteAlbumRoomModel::class], version = 2, exportSchema = false)
internal abstract class AlbumDatabase : RoomDatabase() {
    abstract fun albums(): AlbumDao

    companion object {
        val MIGRATION_1_2 =
            object : Migration(1, 2) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL("CREATE TABLE IF NOT EXISTS `favorite_albums` (`mbId` TEXT NOT NULL, PRIMARY KEY(`mbId`))")
                }
            }
    }
}
