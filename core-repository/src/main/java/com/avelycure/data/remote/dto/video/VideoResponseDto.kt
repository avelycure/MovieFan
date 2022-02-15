package com.avelycure.data.remote.dto.video

import kotlinx.serialization.Serializable

@Serializable
data class VideoResponseDto(
    val id: Int,
    val results: List<VideoResultDto>
)