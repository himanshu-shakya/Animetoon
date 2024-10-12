package com.animetoon.app.data.db.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import com.animetoon.app.data.model.FavoriteWebtoon

@Dao
interface WebtoonDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(webtoon: FavoriteWebtoon)

    @Delete
    suspend fun removeFavorite(webtoon: FavoriteWebtoon)

    @Query("SELECT * FROM favorite_webtoons")
    suspend fun getAllFavorites(): List<FavoriteWebtoon>

    @Query("SELECT * FROM favorite_webtoons WHERE id = :webtoonId LIMIT 1")
    suspend fun isFavorite(webtoonId: Int): FavoriteWebtoon?
}
