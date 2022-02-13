package com.avelycure.data.remote.dto.movie_info

import kotlinx.serialization.Serializable

@Serializable
data class ProductionCountriesDto(
    val iso_3166_1: String,
    val name: String
)