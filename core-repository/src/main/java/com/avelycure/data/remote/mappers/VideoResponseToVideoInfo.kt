package com.avelycure.data.remote.mappers

import com.avelycure.data.remote.dto.video.VideoResponseDto
import com.avelycure.data.remote.dto.video.VideoResultDto
import com.avelycure.domain.models.VideoInfo

fun VideoResultDto.toVideoInfo():VideoInfo{
    return VideoInfo(
        key = key
    )
}