package com.avelycure.domain.repository

import com.avelycure.domain.models.MovieInfo

interface IMovieInfoRepository {
    suspend fun getDetails(id: Int): MovieInfo
}