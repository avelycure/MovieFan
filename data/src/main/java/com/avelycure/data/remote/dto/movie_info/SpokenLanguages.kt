package com.avelycure.data.remote.dto.movie_info

import kotlinx.serialization.Serializable

@Serializable
data class SpokenLanguagesDto(
    val english_name: String,
    val iso_639_1: String,
    val name: String
)