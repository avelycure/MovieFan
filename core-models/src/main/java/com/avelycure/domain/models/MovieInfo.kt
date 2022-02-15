package com.avelycure.domain.models

/**
 * Class which represents detailed information of the movie
 */
data class MovieInfo(
    val adult: Boolean,
    val budget: Int,
    val imdbId: String,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Float,
    val genres: List<String>,
    val productionCompanies: List<String>,
    val productionCountries: List<String>,
    val releaseDate: String,
    val status: String,
    val revenue: Int,
    val tagline: String?,
    val title: String,
    val voteAverage: Float,
    val voteCount: Int,
    val posterPath: String,
    val cast: List<String>,
    val movieId: Int,
    val imagesBackdrop: List<String>,
    val imagesPosters: List<String>,
    val similar: List<Movie>
)