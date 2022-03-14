package com.avelycure.data.repository

import com.avelycure.data.constants.TranslationConstants
import com.avelycure.data.remote.mappers.*
import com.avelycure.data.remote.service.movie.MovieInfoService
import com.avelycure.data.remote.service.movie.PopularMovieService
import com.avelycure.data.remote.service.movie.VideoService
import com.avelycure.data.remote.service.person.PersonInfoService
import com.avelycure.data.remote.service.person.PopularPersonService
import com.avelycure.domain.models.*
import com.avelycure.domain.repository.IRepository
import javax.inject.Singleton

internal class MovieRepository(
    private val popularMovieService: PopularMovieService,
    private val movieInfoService: MovieInfoService,
    private val popularPersonService: PopularPersonService,
    private val videoService: VideoService,
    private val personInfoService: PersonInfoService
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

    override suspend fun getPersonInfo(id: Int): PersonInfo{
        return personInfoService.getPersonInfo(id).toPersonInfo()
    }

}