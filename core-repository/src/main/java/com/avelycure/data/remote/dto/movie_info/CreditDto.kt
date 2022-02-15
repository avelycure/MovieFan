package com.avelycure.data.remote.dto.movie_info
import kotlinx.serialization.Serializable

@Serializable
data class CreditDto(
    val cast: List<CastDto>,
    val crew: List<CrewDto>
)