package com.avelycure.domain.repository

import com.avelycure.domain.models.Movie

interface IMovieRepository {
    suspend fun getPopularMovies(nextPage: Int): List<Movie>
}