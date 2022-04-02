package com.avelycure.data.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import com.avelycure.data.local.MovieDbHelper
import com.avelycure.data.local.MovieInfoDbHelper
import com.avelycure.data.local.PersonDbHelper
import com.avelycure.data.local.SQLiteConstants.PAGE_SIZE
import com.avelycure.data.local.SQLiteConstants.TABLE_NAME_MOVIES
import com.avelycure.data.remote.mappers.*
import com.avelycure.data.remote.service.movie.MovieInfoService
import com.avelycure.data.remote.service.movie.PopularMovieService
import com.avelycure.data.remote.service.movie.VideoService
import com.avelycure.data.remote.service.person.PersonInfoService
import com.avelycure.data.remote.service.person.PopularPersonService
import com.avelycure.domain.models.*
import com.avelycure.domain.repository.IRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

//todo add suspend cancellable coroutine
internal class AppRepository(
    private val popularMovieService: PopularMovieService,
    private val movieInfoService: MovieInfoService,
    private val popularPersonService: PopularPersonService,
    private val videoService: VideoService,
    private val personInfoService: PersonInfoService,
    private val movieDbHelper: MovieDbHelper,
    private val movieInfoDbHelper: MovieInfoDbHelper,
    private val personDbHelper: PersonDbHelper
) : IRepository {

    override suspend fun getPopularMovies(nextPage: Int): List<Movie> {
        return try {
            getPopularMoviesFromRemoteSource(nextPage)
        } catch (e: Exception) {
            Log.d("mytag", "Exception in repo(popular movies): ${e.message}")
            getPopularMoviesFromDb(nextPage - 1)
        }
    }

    override suspend fun getPersonInfo(id: Int): PersonInfo {
        return try {
            getPersonInfoFromRemoteSource(id)
        } catch (e: Exception) {
            Log.d("mytag", "Exception in repo(person info): ${e.message}")
            getPersonInfoFromDb(id)
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

    override suspend fun getTrailerCode(id: Int): VideoInfo {
        return try {
            val videos = videoService.getVideos(id).results
            if (videos.isNotEmpty())
                videos[0].toVideoInfo()
            else
                VideoInfo()
        } catch (e: Exception) {
            Log.d("mytag", "Exception in repo(trailer code): ${e.message}")
            VideoInfo()
        }
    }

    override suspend fun getPopularPersons(page: Int): List<Person> {
        return try {
            getPopularPersonsFromRemoteSource(page)
        } catch (e: Exception) {
            Log.d("mytag", "Exception in repo(popular persons): ${e.message}")
            getPopularPersonsFromDb(page)
        }
    }

    private suspend fun getPopularPersonsFromRemoteSource(page: Int): List<Person> {
        return popularPersonService
            .getPopularPerson(page)
            .results
            .map {
                it.toPerson()
            }.onEach {
                savePersonToLocalDb(it)
            }
    }

    private suspend fun getMovieInfoFromRemoteSource(id: Int): MovieInfo {
        val movieInfo = movieInfoService
            .getMovieDetail(id)
            .toMovieInfo()
        saveMovieInfoToLocalDb(movieInfo)
        return movieInfo
    }

    private suspend fun getPersonInfoFromRemoteSource(id: Int): PersonInfo {
        val personInfo = personInfoService
            .getPersonInfo(id)
            .toPersonInfo()
        savePersonInfoToLocalDb(personInfo)
        return personInfo
    }

    private suspend fun getPopularMoviesFromRemoteSource(nextPage: Int): List<Movie> {
        val result = popularMovieService
            .getPopularMovies(nextPage)
            .results
            .map { movieResult ->
                movieResult.toMovie()
            }

        result
            .takeLast(PAGE_SIZE)
            .onEach {
                saveMovieToLocalDb(it)
            }

        return result
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @SuppressLint("Recycle")
    private suspend fun getPopularMoviesFromDb(nextPage: Int): List<Movie> {
        return suspendCoroutine { continuation ->
            val db = movieDbHelper.writableDatabase
            val cursor = db.query(TABLE_NAME_MOVIES, null, null, null, null, null, null)
            val result = mutableListOf<Movie>()

            //number of the beging element
            val beginIndex = PAGE_SIZE * nextPage
            val endIndex = PAGE_SIZE * (nextPage + 1)

            if (cursor.moveToPosition(beginIndex)) {
                val idColIndex = cursor.getColumnIndex("id")
                val titleColIndex = cursor.getColumnIndex("title")
                val originalTitleColIndex = cursor.getColumnIndex("original_title")
                val posterPathColIndex = cursor.getColumnIndex("poster_path")
                val genresColIndex = cursor.getColumnIndex("genres")
                val popularityColIndex = cursor.getColumnIndex("popularity")
                val voteAverageColIndex = cursor.getColumnIndex("vote_average")
                val releaseDateColIndex = cursor.getColumnIndex("release_date")
                val movieIdColIndex = cursor.getColumnIndex("movie_id")
                val voteCountColumnIndex = cursor.getColumnIndex("vote_count")

                var i = 0

                do {
                    result.add(
                        Movie(
                            cursor.getString(titleColIndex),
                            cursor.getString(originalTitleColIndex),
                            cursor.getString(posterPathColIndex),
                            emptyList(),
                            cursor.getFloat(popularityColIndex),
                            cursor.getFloat(voteAverageColIndex),
                            cursor.getString(releaseDateColIndex),
                            cursor.getInt(movieIdColIndex),
                            cursor.getInt(voteCountColumnIndex)
                        )
                    )
                    i++
                } while (cursor.moveToNext() && i < endIndex)
            }
            cursor.close()
            Log.d("mytag", "Res size: " + result.size)
            continuation.resume(result)
        }
    }

    private fun getPersonInfoFromDb(id: Int): PersonInfo {
        return PersonInfo(
            "", "", "", 0, "", emptyList(), -1, "",
            0f, "", "", true, "", "", emptyList()
        )
    }

    private fun getPopularPersonsFromDb(page: Int): List<Person> {
        return emptyList()
    }

    private suspend fun getMovieInfoFromDb(id: Int): MovieInfo {
        return MovieInfo(
            false, 0, "", "", "", "", 0f, emptyList(),
            emptyList(), emptyList(), "", "", 0, "", "", 0f, 0, "", emptyList(),
            0, emptyList(), emptyList(), emptyList()
        )
    }

    private fun saveMovieToLocalDb(movie: Movie) {
        val cv = ContentValues()
        val db = movieDbHelper.writableDatabase
        cv.put("title", movie.title)
        cv.put("original_title", movie.originalTitle)
        cv.put("poster_path", movie.posterPath)
        cv.put("genres", movie.genres.toString())
        cv.put("popularity", movie.popularity)
        cv.put("vote_average", movie.voteAverage)
        cv.put("release_date", movie.releaseDate)
        cv.put("movie_id", movie.movieId)
        cv.put("vote_count", movie.voteCount)
        db.insert(TABLE_NAME_MOVIES, null, cv)
    }

    private fun savePersonToLocalDb(person: Person) {

    }

    private fun savePersonInfoToLocalDb(personInfo: PersonInfo) {

    }

    private fun saveMovieInfoToLocalDb(movieInfo: MovieInfo) {

    }

}