package com.avelycure.data.remote.dto.movie_info

import kotlinx.serialization.Serializable

@Serializable
data class ProductionCompaniesDto(
    val name: String,
    val id: Int,
    val logo_path: String?,
    val origin_country: String
)