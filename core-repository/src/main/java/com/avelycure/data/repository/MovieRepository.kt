package com.avelycure.data.repository

import com.avelycure.data.remote.mappers.toPerson
import com.avelycure.data.remote.mappers.toMovie
import com.avelycure.data.remote.mappers.toMovieInfo
import com.avelycure.data.remote.mappers.toVideoInfo
import com.avelycure.data.remote.service.movie.MovieInfoService
import com.avelycure.data.remote.service.movie.PopularMovieService
import com.avelycure.data.remote.service.movie.VideoService
import com.avelycure.data.remote.service.person.PopularPersonService
import com.avelycure.domain.models.Movie
import com.avelycure.domain.models.MovieInfo
import com.avelycure.domain.models.Person
import com.avelycure.domain.models.VideoInfo
import com.avelycure.domain.repository.IRepository
import javax.inject.Singleton

internal class MovieRepository(
    private val popularMovieService: PopularMovieService,
    private val movieInfoService: MovieInfoService,
    private val popularPersonService: PopularPersonService,
    private val videoService: VideoService
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

    override suspend fun getTrailerCode(id: Int): VideoInfo {
        val videos = videoService.getVideos(id).results
        if (videos.isNotEmpty())
            return videos[0].toVideoInfo()
        else
            return VideoInfo()
    }

    override suspend fun getPopularPersons(page: Int): List<Person> {
        return popularPersonService.getPopularPerson(page).results.map {
            it.toPerson()
        }
    }

}