package com.avelycure.data.remote.dto.movie_info

import kotlinx.serialization.Serializable

@Serializable
data class MovieGenreDto(
    val id: Int,
    val name: String
)