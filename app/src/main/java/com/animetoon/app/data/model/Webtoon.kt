package com.animetoon.app.data.model

data class Webtoon(
    val id: Int = 0,
    val title: String = "",
    val imageUrl: String = "",
    val briefDescription: String = "",
    val detailedDescription: String = "",
    val creator: String = "",
    val reads: String = "",
    var averageRating: Double = 0.0,
    val totalRatings: Int = 0,
    val totalScore: Int = 0
)

fun Webtoon.toFavoriteWebtoon(): FavoriteWebtoon {
    return FavoriteWebtoon(
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