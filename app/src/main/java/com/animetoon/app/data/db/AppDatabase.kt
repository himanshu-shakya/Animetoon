package com.animetoon.app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.animetoon.app.data.db.dao.WebtoonDao
import com.animetoon.app.data.model.FavoriteWebtoon

@Database(entities = [FavoriteWebtoon::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun webtoonDao(): WebtoonDao
}
