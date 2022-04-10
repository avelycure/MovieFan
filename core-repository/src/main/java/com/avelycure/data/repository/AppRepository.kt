package com.avelycure.data.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import com.avelycure.data.local.AppDbHelper
import com.avelycure.data.local.SQLiteConstants
import com.avelycure.data.local.SQLiteConstants.PAGE_SIZE
import com.avelycure.data.local.SQLiteConstants.TABLE_NAME_MOVIES
import com.avelycure.data.local.SQLiteConstants.TABLE_NAME_MOVIE_INFO
import com.avelycure.data.local.SQLiteConstants.TABLE_NAME_PERSONS
import com.avelycure.data.remote.mappers.*
import com.avelycure.data.remote.service.movie.MovieInfoService
import com.avelycure.data.remote.service.movie.PopularMovieService
import com.avelycure.data.remote.service.movie.SearchMovieService
import com.avelycure.data.remote.service.movie.VideoService
import com.avelycure.data.remote.service.person.PersonInfoService
import com.avelycure.data.remote.service.person.PopularPersonService
import com.avelycure.data.remote.service.person.SearchPersonService
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
    private val searchMovieService: SearchMovieService,
    private val appDbHelper: AppDbHelper,
    private val searchPerson: SearchPersonService
) : IRepository {

    override suspend fun getPopularMovies(nextPage: Int): List<Movie> {
        return try {
            getPopularMoviesFromRemoteSource(nextPage)
        } catch (e: Exception) {
            Log.d("mytag", "Exception in repo(popular movies): ${e.message}")
            getPopularMoviesFromDb(nextPage - 1)
        }
    }

    override suspend fun searchMovie(title: String, page: Int): List<Movie> {
        return searchMovieService.getMovieByName(title, page).results.map { it.toMovie() }
    }

    override suspend fun getPersonInfo(id: Int): PersonInfo {
        return try {
            getPersonInfoFromRemoteSource(id)
        } catch (e: Exception) {
            Log.d("mytag", "Exception in repo(person info): ${e.message}")
            getPersonInfoFromDb(id)
        }
    }

    override suspend fun searchPerson(query: String, page: Int): List<Person> {
        return try {
            searchPersonsInRemoteSource(query, page)
        }catch (e: Exception){
            Log.d("mytag", "Exception in repo(search person): ${e.message}")
            searchPersonsInDb(query, page)
        }
    }

    private fun searchPersonsInDb(query: String, page: Int): List<Person> {
        return emptyList()
    }

    private suspend fun searchPersonsInRemoteSource(query: String, page: Int): List<Person> {
        return searchPerson.getPersonsByName(query, page).results.map { it.toPerson() }
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
        return suspendCoroutine { continuation ->
            val db = appDbHelper.writableDatabase
            val cursor = db.query(
                TABLE_NAME_PERSONS,
                null,
                "person_id = ?",
                arrayOf(id.toString()),
                null,
                null,
                null
            )
            var result: PersonInfo? = null

            if (cursor.moveToFirst()) {
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

                result = PersonInfo(
                    cursor.getString(birthdayColIndex),
                    cursor.getString(knownForDepartmentColIndex),
                    cursor.getString(deathDayColIndex),
                    cursor.getInt(personIdColIndex),
                    cursor.getString(nameColIndex),
                    emptyList(),
                    cursor.getInt(genderColIndex),
                    cursor.getString(biographyColIndex),
                    cursor.getFloat(popularityColIndex),
                    cursor.getString(placeOfBirthColIndex),
                    cursor.getString(profilePathColIndex),
                    cursor.getInt(adultColIndex).toBoolean(),
                    cursor.getString(imdbIdColIndex),
                    cursor.getString(homepageColIndex),
                    emptyList()
                )
            }
            cursor.close()
            continuation.resume(result ?: PersonInfo())
        }
    }

    private suspend fun getPopularPersonsFromDb(page: Int): List<Person> {
        return suspendCoroutine { continuation ->
            val db = appDbHelper.writableDatabase
            val cursor = db.query(TABLE_NAME_PERSONS, null, null, null, null, null, null)
            val result = mutableListOf<Person>()

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

                var i = 0
                do {
                    result.add(
                        Person(
                            cursor.getInt(personIdColIndex),
                            cursor.getString(profilePathColIndex),
                            cursor.getInt(adultColIndex).toBoolean(),
                            cursor.getString(nameColIndex),
                            cursor.getFloat(popularityColIndex),
                            cursor.getString(knownForDepartmentColIndex),
                            emptyList(),
                            emptyList()
                        )
                    )
                    i++
                } while (cursor.moveToNext() && i < endIndex)
            }

            cursor.close()
            continuation.resume(result)
        }
    }

    @SuppressLint("Recycle")
    private suspend fun getMovieInfoFromDb(id: Int): MovieInfo {
        return suspendCoroutine { continuation ->
            val db = appDbHelper.writableDatabase
            val cursor = db.query(
                SQLiteConstants.TABLE_NAME_MOVIE_INFO,
                null,
                "movie_id = ?",
                arrayOf(id.toString()),
                null,
                null,
                null
            )
            var result: MovieInfo? = null

            if (cursor.moveToFirst()) {
                val adultColIndex = cursor.getColumnIndex("adult")
                val budgetColIndex = cursor.getColumnIndex("budget")
                val imdbIdColIndex = cursor.getColumnIndex("imdb_id")
                val originalLangColIndex = cursor.getColumnIndex("original_lang")
                val originalTitleColIndex = cursor.getColumnIndex("original_title")
                val overviewColIndex = cursor.getColumnIndex("overview")
                val popularityColIndex = cursor.getColumnIndex("popularity")
                val genresColIndex = cursor.getColumnIndex("genres")
                val pCompaniesColIndex = cursor.getColumnIndex("p_companies")
                val pCountriesColIndex = cursor.getColumnIndex("p_countries")
                val releaseDateColIndex = cursor.getColumnIndex("release_date")
                val statusColIndex = cursor.getColumnIndex("status")
                val revenueColIndex = cursor.getColumnIndex("revenue")
                val taglineColIndex = cursor.getColumnIndex("tagline")
                val titleColIndex = cursor.getColumnIndex("title")
                val voteAverageColIndex = cursor.getColumnIndex("vote_average")
                val voteCountColIndex = cursor.getColumnIndex("vote_count")
                val posterPathColIndex = cursor.getColumnIndex("poster_path")
                val movieIdColIndex = cursor.getColumnIndex("movie_id")
                val castIdColIndex = cursor.getColumnIndex("film_cast")
                val imagesBackdropColIndex = cursor.getColumnIndex("images_backdrop")
                val imagesPostersColIndex = cursor.getColumnIndex("images_posters")

                result = MovieInfo(
                    cursor.getInt(adultColIndex).toBoolean(),
                    cursor.getInt(budgetColIndex),
                    cursor.getString(imdbIdColIndex),
                    cursor.getString(originalLangColIndex),
                    cursor.getString(originalTitleColIndex),
                    cursor.getString(overviewColIndex),
                    cursor.getFloat(popularityColIndex),
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    cursor.getString(releaseDateColIndex),
                    cursor.getString(statusColIndex),
                    cursor.getInt(revenueColIndex),
                    cursor.getString(taglineColIndex),
                    cursor.getString(titleColIndex),
                    cursor.getFloat(voteAverageColIndex),
                    cursor.getInt(voteCountColIndex),
                    cursor.getString(posterPathColIndex),
                    emptyList(),
                    cursor.getInt(movieIdColIndex),
                    emptyList(),
                    emptyList(),
                    emptyList()
                )
            }

            continuation.resume(
                result ?: MovieInfo(
                    false, 0, "", "", "", "", 0f, emptyList(),
                    emptyList(), emptyList(), "", "", 0, "", "", 0f, 0, "", emptyList(),
                    0, emptyList(), emptyList(), emptyList()
                )
            )
        }
    }

    @SuppressLint("Recycle")
    private fun saveMovieToLocalDb(movie: Movie) {
        val cv = ContentValues()
        val db = appDbHelper.writableDatabase

        val cursor = db.query(
            TABLE_NAME_MOVIES,
            null,
            "movie_id = ?",
            arrayOf(movie.movieId.toString()),
            null,
            null,
            null
        )

        //only if there is no such element in db
        if (!cursor.moveToFirst()) {
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
    }

    @SuppressLint("Recycle")
    private fun savePersonToLocalDb(person: Person) {
        val cv = ContentValues()
        val db = appDbHelper.writableDatabase

        val cursor = db.query(
            TABLE_NAME_PERSONS,
            null,
            "person_id = ?",
            arrayOf(person.id.toString()),
            null,
            null,
            null
        )

        //if no such users in db then add
        if (!cursor.moveToFirst()) {
            cv.put("person_id", person.id)
            cv.put("profile_path", person.profilePath)
            cv.put("adult", 1)
            cv.put("name", person.name)
            cv.put("popularity", person.popularity)
            cv.put("known_for_department", person.knownForDepartment)
            cv.put("known_for_movie", person.knownForMovie.toString())
            cv.put("known_for_tv", person.knownForTv.toString())

            cv.put("expanded", false)
            cv.put("birthday", "")
            cv.put("death_day", "")
            cv.put("also_known_as", "")
            cv.put("gender", "")
            cv.put("biography", "")
            cv.put("place_of_birth", "")
            cv.put("imdb_id", "")
            cv.put("homepage", "")
            cv.put("profile_images", "")
            db.insert(TABLE_NAME_PERSONS, null, cv)
        }
    }

    @SuppressLint("Recycle")
    private fun savePersonInfoToLocalDb(personInfo: PersonInfo) {
        val cv = ContentValues()
        val db = appDbHelper.writableDatabase
        cv.put("expanded", false)
        cv.put("birthday", personInfo.birthday)
        cv.put("death_day", personInfo.deathDay)
        cv.put("also_known_as", personInfo.alsoKnownAs.toString())
        cv.put("gender", personInfo.gender)
        cv.put("biography", personInfo.biography)
        cv.put("place_of_birth", personInfo.placeOfBirth)
        cv.put("imdb_id", personInfo.imdbId)
        cv.put("homepage", personInfo.homepage)
        cv.put("profile_images", personInfo.profileImages.toString())
        db.update(TABLE_NAME_PERSONS, cv, "person_id = ?", arrayOf(personInfo.id.toString()))
    }

    @SuppressLint("Recycle")
    private fun saveMovieInfoToLocalDb(movieInfo: MovieInfo) {
        val cv = ContentValues()
        val db = appDbHelper.writableDatabase

        val cursor = db.query(
            TABLE_NAME_MOVIE_INFO,
            null,
            "movie_id = ?",
            arrayOf(movieInfo.movieId.toString()),
            null,
            null,
            null
        )

        if (!cursor.moveToFirst()) {
            cv.put("adult", movieInfo.adult)
            cv.put("budget", movieInfo.budget)
            cv.put("imdb_id", movieInfo.imdbId)
            cv.put("original_lang", movieInfo.originalLanguage)
            cv.put("original_title", movieInfo.originalTitle)
            cv.put("overview", movieInfo.overview)
            cv.put("popularity", movieInfo.popularity)
            cv.put("genres", movieInfo.genres.toString())
            cv.put("p_companies", movieInfo.productionCompanies.toString())
            cv.put("p_countries", movieInfo.productionCountries.toString())
            cv.put("release_date", movieInfo.releaseDate)
            cv.put("status", movieInfo.status)
            cv.put("revenue", movieInfo.revenue)
            cv.put("tagline", movieInfo.tagline)
            cv.put("title", movieInfo.title)
            cv.put("vote_average", movieInfo.voteAverage)
            cv.put("vote_count", movieInfo.voteCount)
            cv.put("poster_path", movieInfo.posterPath)
            cv.put("film_cast", movieInfo.cast.toString())
            cv.put("movie_id", movieInfo.movieId)
            cv.put("images_backdrop", movieInfo.imagesBackdrop.toString())
            cv.put("images_posters", movieInfo.imagesPosters.toString())

            db.insert(TABLE_NAME_MOVIE_INFO, null, cv)
        }
    }

}

private fun Int.toBoolean(): Boolean {
    return this != 0
}
