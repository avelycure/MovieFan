package com.avelycure.data.remote.dto.person

import com.avelycure.data.utils.KnownForSerializer
import kotlinx.serialization.Serializable

@Serializable(with = KnownForSerializer::class)
sealed class KnownFor {
    abstract val poster_path: String?
    abstract val overview: String
    abstract val genre_ids: List<Int>
    abstract val id: Int
    abstract val media_type: String
    abstract val original_language: String
    abstract val backdrop_path: String?
    abstract val popularity: Float
    abstract val vote_count: Int
    abstract val vote_average: Float
}
