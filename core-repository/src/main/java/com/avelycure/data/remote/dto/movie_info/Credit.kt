package com.avelycure.data.remote.dto.movie_info
import kotlinx.serialization.Serializable

@Serializable
data class Credit(
    val cast: List<Cast>,
    val crew: List<Crew>
)