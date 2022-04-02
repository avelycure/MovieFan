package com.avelycure.data.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import com.avelycure.data.local.AppDbHelper
import com.avelycure.data.local.SQLiteConstants.PAGE_SIZE
import com.avelycure.data.local.SQLiteConstants.TABLE_NAME_MOVIES
import com.avelycure.data.local.SQLiteConstants.TABLE_NAME_PERSONS
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

internal class AppRepository(
    private val popularMovieService: PopularMovieService,
    private val movieInfoService: MovieInfoService,
    private val popularPersonService: PopularPersonService,
    private val videoService: VideoService,
    private val personInfoService: PersonInfoService,
    private val appDbHelper: AppDbHelper,
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
        //return try {
        return getPersonInfoFromRemoteSource(id)
        //} catch (e: Exception) {
        // Log.d("mytag", "Exception in repo(person info): ${e.message}")
        //getPersonInfoFromDb(id)
        // }
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
            getPopularPersonsFromDb(page - 1)
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
            val db = appDbHelper.writableDatabase
            val cursor = db.query(TABLE_NAME_MOVIES, null, null, null, null, null, null)
            val result = mutableListOf<Movie>()

            Log.d("mytag", "cursor1: " + cursor.count)


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
            continuation.resume(result)
        }
    }

    private suspend fun getPersonInfoFromDb(id: Int): PersonInfo {
        return PersonInfo()
    }

    private suspend fun getPopularPersonsFromDb(page: Int): List<Person> {
        return suspendCoroutine { continuation ->
            val db = appDbHelper.writableDatabase
            val cursor = db.query(TABLE_NAME_PERSONS, null, null, null, null, null, null)
            val result = mutableListOf<Person>()

            Log.d("mytag", "cursor: " + cursor.count)

            //number of the start element
            val beginIndex = PAGE_SIZE * page
            val endIndex = PAGE_SIZE * (page + 1)

            if (cursor.moveToPosition(beginIndex)) {
                val personIdColIndex = cursor.getColumnIndex("person_id")
                val profilePathColIndex = cursor.getColumnIndex("profile_path")
                val adultColIndex = cursor.getColumnIndex("adult")
                val nameColIndex = cursor.getColumnIndex("name")
                val popularityColIndex = cursor.getColumnIndex("popularity")
                val knownForDepartmentColIndex = cursor.getColumnIndex("known_for_department")
                val knownForMovieColIndex = cursor.getColumnIndex("known_for_movie")
                val knownForTvColIndex = cursor.getColumnIndex("known_for_tv")
                val expandedIdColIndex = cursor.getColumnIndex("expanded")
                val birthdayColIndex = cursor.getColumnIndex("birthday")
                val deathDayColIndex = cursor.getColumnIndex("death_day")
                val alsoKnownAsColIndex = cursor.getColumnIndex("also_known_as")
                val genderColIndex = cursor.getColumnIndex("gender")
                val biographyColIndex = cursor.getColumnIndex("biography")
                val placeOfBirthColIndex = cursor.getColumnIndex("place_of_birth")
                val imdbIdColIndex = cursor.getColumnIndex("imdb_id")
                val homepageColIndex = cursor.getColumnIndex("homepage")
                val profileImagesColIndex = cursor.getColumnIndex("profile_images")

                var i = 0
                do {
                    result.add(Person(
                        cursor.getInt(personIdColIndex),
                        cursor.getString(profilePathColIndex),
                        cursor.getInt(adultColIndex).toBoolean(),
                        cursor.getString(nameColIndex),
                        cursor.getFloat(popularityColIndex),
                        cursor.getString(knownForDepartmentColIndex),
                        emptyList(),
                        emptyList()
                    ).apply {
                        expanded = false
                        birthday = cursor.getString(birthdayColIndex)
                        deathDay = cursor.getString(deathDayColIndex)
                        alsoKnownAs = emptyList()
                        gender = cursor.getInt(genderColIndex)
                        biography = cursor.getString(biographyColIndex)
                        placeOfBirth = cursor.getString(placeOfBirthColIndex)
                        imdbId = cursor.getString(imdbIdColIndex)
                        homepage = cursor.getString(homepageColIndex)
                        profileImages = emptyList()
                    })
                    i++
                } while (cursor.moveToNext() && i < endIndex)
            }

            cursor.close()
            Log.d("mytag", "size: " + result.size)
            continuation.resume(result)
        }
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
        val db = appDbHelper.writableDatabase
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
        val cv = ContentValues()
        val db = appDbHelper.writableDatabase
        cv.put("person_id", person.id)
        cv.put("profile_path", person.profilePath)
        cv.put("adult", 1)
        cv.put("name", person.name)
        cv.put("popularity", person.popularity)
        cv.put("known_for_department", person.knownForDepartment)
        cv.put("known_for_movie", person.knownForMovie.toString())
        cv.put("known_for_tv", person.knownForTv.toString())
        cv.put("expanded", person.expanded)
        cv.put("birthday", person.birthday)
        cv.put("death_day", person.deathDay)
        cv.put("also_known_as", person.alsoKnownAs.toString())
        cv.put("gender", person.gender)
        cv.put("biography", person.biography)
        cv.put("place_of_birth", person.placeOfBirth)
        cv.put("imdb_id", person.imdbId)
        cv.put("homepage", person.homepage)
        cv.put("profile_images", person.profileImages.toString())
        db.insert(TABLE_NAME_PERSONS, null, cv)
    }

    private fun savePersonInfoToLocalDb(personInfo: PersonInfo) {

    }

    private fun saveMovieInfoToLocalDb(movieInfo: MovieInfo) {

    }

}

private fun Int.toBoolean(): Boolean {
    return this != 0
}
