package com.avelycure.data.repository

import android.util.Log
import com.avelycure.data.remote.mappers.toMovie
import com.avelycure.data.remote.service.PopularMovieService
import com.avelycure.domain.models.Movie
import com.avelycure.domain.repository.IMovieRepository

internal class MovieRepository(
    private val popularMovieService: PopularMovieService
) : IMovieRepository {

    override suspend fun getPopularMovies(nextPage: Int): List<Movie> {
        Log.d("mytag", "made request")
        return popularMovieService
            .getPopularMovies(nextPage)
            .results
            .map { movieResult ->
                movieResult.toMovie()
            }
    }

}