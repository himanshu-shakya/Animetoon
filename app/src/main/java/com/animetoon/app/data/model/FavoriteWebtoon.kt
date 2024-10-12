package com.animetoon.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "favorite_webtoons")
data class FavoriteWebtoon(
    @PrimaryKey(autoGenerate = false) val id: Int = 0,
    val title: String = "",
    val imageUrl: String = "",
    val briefDescription: String = "",
    val detailedDescription: String = "",
    val creator: String = "",
    val reads: String = "",
    val averageRating: Double = 0.0,
    val totalRatings: Int = 0,
    val totalScore: Int = 0
)
fun FavoriteWebtoon.toWebtoon(): Webtoon {
    return Webtoon(
        id = this.id,
        title = this.title,
        imageUrl = this.imageUrl,
        briefDescription = this.briefDescription,
        detailedDescription = this.detailedDescription,
        creator = this.creator,
        reads = this.reads,
        averageRating = this.averageRating,
        totalRatings = this.totalRatings,
        totalScore = this.totalScore
    )
}


