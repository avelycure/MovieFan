package com.avelycure.data.remote.dto.movie_info

import com.avelycure.data.remote.dto.movie.MovieResponse
import kotlinx.serialization.Serializable

@Serializable
data class MovieInfo(
    val adult: Boolean,
    val backdrop_path: String?,
    val belongs_to_collection: MovieCollection?,
    val budget: Int,
    val genres: List<MovieGenreDto>,
    val homepage: String?,
    val id: Int,
    val imdb_id: String?,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Float,
    val poster_path: String?,
    val production_companies: List<ProductionCompaniesDto>,
    val production_countries: List<ProductionCountriesDto>,
    val release_date: String,
    val revenue: Int,
    val runtime: Int?,
    val spoken_languages: List<SpokenLanguagesDto>,
    val status: String,
    val tagline: String?,
    val title: String,
    val video: Boolean,
    val vote_average: Float,
    val vote_count: Int,
    val credits: Credit,
    val images: MovieImages,
    val similar: MovieResponse
)