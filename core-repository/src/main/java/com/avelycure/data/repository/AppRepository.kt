package com.avelycure.data.repository

import android.util.Log
import com.avelycure.data.remote.mappers.*
import com.avelycure.data.remote.service.movie.MovieInfoService
import com.avelycure.data.remote.service.movie.PopularMovieService
import com.avelycure.data.remote.service.movie.VideoService
import com.avelycure.data.remote.service.person.PersonInfoService
import com.avelycure.data.remote.service.person.PopularPersonService
import com.avelycure.domain.models.*
import com.avelycure.domain.repository.IRepository
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.io.IOException

fun <T> Flow<T>.handleErrors(): Flow<T> =
    catch { e -> Log.d("mytag", "got error") }

internal class AppRepository(
    private val popularMovieService: PopularMovieService,
    private val movieInfoService: MovieInfoService,
    private val popularPersonService: PopularPersonService,
    private val videoService: VideoService,
    private val personInfoService: PersonInfoService
) : IRepository {

    override suspend fun getPopularMovies(nextPage: Int): List<Movie> {
        return try {
            getPopularMoviesFromRemoteSource(nextPage)
        } catch (e: Exception) {
            Log.d("mytag", "Exception in repo(popular movies): ${e.message}")
            getPopularMoviesFromDb(nextPage)
        }
    }

    private suspend fun getPopularMoviesFromDb(nextPage: Int): List<Movie> {
        return emptyList()
    }

    private fun saveToLocalDb(movie: Movie) {

    }

    private suspend fun getPopularMoviesFromRemoteSource(nextPage: Int): List<Movie> {
        return popularMovieService
            .getPopularMovies(nextPage)
            .results
            .map { movieResult ->
                movieResult.toMovie()
            }
            .onEach {
                saveToLocalDb(it)
            }
    }

    override suspend fun getDetails(id: Int): MovieInfo {
        return try {
            getMovieInfoFromRemoteSource(id)
        } catch (e: Exception) {
            Log.d("mytag", "Exception in repo(movie info): ${e.message}")
            getMovieInfoFromDb(id)
        }
    }

    private suspend fun getMovieInfoFromRemoteSource(id: Int): MovieInfo {
        return movieInfoService
            .getMovieDetail(id)
            .toMovieInfo()
    }

    private suspend fun getMovieInfoFromDb(id: Int): MovieInfo {
        return MovieInfo(
            false, 0, "", "", "", "", 0f, emptyList(),
            emptyList(), emptyList(), "", "", 0, "", "", 0f, 0, "", emptyList(),
            0, emptyList(), emptyList(), emptyList()
        )
    }

    override suspend fun getTrailerCode(id: Int): VideoInfo {
        return try {
            val videos = videoService.getVideos(id).results
            if (videos.isNotEmpty())
                videos[0].toVideoInfo()
            else
                VideoInfo()
        } catch (e: Exception) {
            VideoInfo()
        }
    }

    override suspend fun getPopularPersons(page: Int): List<Person> {
        return try {
            getPopularPersonsFromRemoteSource(page)
        } catch (e: Exception) {
            getPopularPersonsFromDb(page)
        }
    }

    private fun getPopularPersonsFromDb(page: Int): List<Person> {
        return emptyList()
    }

    private suspend fun getPopularPersonsFromRemoteSource(page: Int): List<Person> {
        return popularPersonService
            .getPopularPerson(page)
            .results
            .map {
                it.toPerson()
            }
    }

    override suspend fun getPersonInfo(id: Int): PersonInfo {
        return try {
            getPersonInfoFromRemoteSource(id)
        } catch (e: Exception) {
            getPersonInfoFromDb(id)
        }
    }

    private fun getPersonInfoFromDb(id: Int): PersonInfo {
        return PersonInfo(
            "", "", "", 0, "", emptyList(), -1, "",
            0f, "", "", true, "", "", emptyList()
        )
    }

    private suspend fun getPersonInfoFromRemoteSource(id: Int): PersonInfo {
        return personInfoService
            .getPersonInfo(id)
            .toPersonInfo()
    }


}