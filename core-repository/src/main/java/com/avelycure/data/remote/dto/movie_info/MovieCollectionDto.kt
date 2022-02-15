package com.avelycure.data.remote.dto.movie_info

import kotlinx.serialization.Serializable

@Serializable
data class MovieCollectionDto(
    val id: Int,
    val name: String,
    val poster_path: String?,
    val backdrop_path: String?
)