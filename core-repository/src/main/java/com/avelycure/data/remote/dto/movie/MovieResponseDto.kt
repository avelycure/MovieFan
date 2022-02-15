package com.avelycure.data.remote.dto.movie

import kotlinx.serialization.Serializable

@Serializable
data class MovieResponseDto(
    val page: Int = 0,
    val results: List<MovieResultDto> = emptyList(),
    val total_results: Int = 0,
    val total_pages: Int = 0
)