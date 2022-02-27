package com.avelycure.domain.repository

import com.avelycure.domain.models.MovieInfo
import com.avelycure.domain.models.VideoInfo

interface IMovieInfoRepository {
    suspend fun getDetails(id: Int): MovieInfo
    suspend fun getTrailerCode(id: Int): VideoInfo
}