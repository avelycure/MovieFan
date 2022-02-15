package com.avelycure.data.remote.dto.movie_info

import kotlinx.serialization.Serializable

@Serializable
data class MovieImagesDto(
    val backdrops: List<BackdropDto> = emptyList(),
    val logos: List<LogoDto>,
    val posters: List<PosterDto> = emptyList(),
)