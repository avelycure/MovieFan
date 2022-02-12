package com.avelycure.data.remote.dto.video

import kotlinx.serialization.Serializable

@Serializable
data class VideoResponse(
    val id: Int,
    val results: List<VideoResult>
)