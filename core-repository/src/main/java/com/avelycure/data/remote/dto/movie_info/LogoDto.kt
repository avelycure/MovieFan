package com.avelycure.data.remote.dto.movie_info

import kotlinx.serialization.Serializable

@Serializable
data class LogoDto(
    val aspect_ratio: Float,
    val height: Int,
    val iso_639_1: String?,
    val file_path: String,
    val vote_average: Float,
    val vote_count: Int,
    val width: Int
)