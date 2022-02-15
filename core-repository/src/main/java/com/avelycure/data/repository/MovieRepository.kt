package com.avelycure.data.repository

import com.avelycure.data.remote.mappers.toMovie
import com.avelycure.data.remote.mappers.toMovieInfo
import com.avelycure.data.remote.service.MovieInfoService
import com.avelycure.data.remote.service.PopularMovieService
import com.avelycure.domain.models.Movie
import com.avelycure.domain.models.MovieInfo
import com.avelycure.domain.repository.IMovieInfoRepository
import com.avelycure.domain.repository.IMovieRepository
import com.avelycure.domain.repository.IRepository
import javax.inject.Singleton

@Singleton
internal class MovieRepository(
    private val popularMovieService: PopularMovieService,
    private val movieInfoService: MovieInfoService
) : IRepository {

    override suspend fun getPopularMovies(nextPage: Int): List<Movie> {
        return popularMovieService
            .getPopularMovies(nextPage)
            .results
            .map { movieResult ->
                movieResult.toMovie()
            }
    }

    override suspend fun getDetails(id: Int): MovieInfo {
        return movieInfoService
            .getMovieDetail(id)
            .toMovieInfo()
    }

}