package com.animetoon.app.data.model

data class Webtoon(
    val id: Int,
    val title: String,
    val image_url: String,
    val brief_description: String,
    val detailed_description: String,
    val creator: String,
    val reads: String,
    val average_rating: Double,
    val total_ratings: Int,
    val total_score: Int
)
