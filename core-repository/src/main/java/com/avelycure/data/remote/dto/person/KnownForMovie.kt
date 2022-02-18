package com.avelycure.data.remote.dto.person

import kotlinx.serialization.Serializable

@Serializable
data class KnownForMovie(
    override val poster_path: String? = "",
    val adult: Boolean = false,
    override val overview: String = "",
    val release_date: String = "",
    val original_title: String = "",
    override val genre_ids: List<Int> = emptyList(),
    override val id: Int = 0,
    override val media_type: String = "",
    override val original_language: String = "",
    val title: String = "",
    override val backdrop_path: String? = "",
    override val popularity: Float = 0f,
    override val vote_count: Int = 0,
    val video: Boolean = false,
    override val vote_average: Float = 0f
) : KnownFor()
