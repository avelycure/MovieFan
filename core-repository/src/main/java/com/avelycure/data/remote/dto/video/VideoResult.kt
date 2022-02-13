package com.avelycure.data.remote.dto.video

import kotlinx.serialization.Serializable

@Serializable
data class VideoResult(
    val iso_639_1: String,
    val iso_3166_1: String,
    val name: String,
    val key: String,
    val site: String,
    val size: Int,
    val type: String,
    val official: Boolean,
    val published_at: String,
    val id: String
)