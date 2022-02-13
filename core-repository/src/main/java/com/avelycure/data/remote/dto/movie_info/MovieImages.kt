package com.avelycure.data.remote.dto.movie_info

import kotlinx.serialization.Serializable

@Serializable
data class MovieImages(
    val backdrops: List<Backdrop> = emptyList(),
    val logos: List<Logo>,
    val posters: List<Poster> = emptyList(),
)