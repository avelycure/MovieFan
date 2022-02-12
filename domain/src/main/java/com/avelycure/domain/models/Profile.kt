package com.avelycure.domain.models

data class Profile(
    val aspect_ratio: Float,
    val file_path: String,
    val height: Int,
    val iso_639_1: Int?,
    val vote_average: Float,
    val vote_count: Int,
    val width: Int
)