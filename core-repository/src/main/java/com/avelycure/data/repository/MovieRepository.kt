package com.avelycure.data.repository

import com.avelycure.data.remote.mappers.toPerson
import com.avelycure.data.remote.mappers.toMovie
import com.avelycure.data.remote.mappers.toMovieInfo
import com.avelycure.data.remote.service.movie.MovieInfoService
import com.avelycure.data.remote.service.movie.PopularMovieService
import com.avelycure.data.remote.service.person.PopularPersonService
import com.avelycure.domain.models.Movie
import com.avelycure.domain.models.MovieInfo
import com.avelycure.domain.models.Person
import com.avelycure.domain.repository.IRepository
import javax.inject.Singleton

@Singleton
internal class MovieRepository(
    private val popularMovieService: PopularMovieService,
    private val movieInfoService: MovieInfoService,
    private val popularPersonService: PopularPersonService,
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

    override suspend fun getPopularPersons(page: Int): List<Person> {
        return popularPersonService.getPopularPerson(page).results.map {
            it.toPerson()
        }
    }
}