package com.avelycure.data.remote.dto.movie_info

import com.avelycure.data.remote.dto.movie.MovieResponseDto
import kotlinx.serialization.Serializable

//todo check default values
@Serializable
data class MovieInfoDto(
    val adult: Boolean = false,
    val backdrop_path: String? = "",
    val belongs_to_collection: MovieCollectionDto? = MovieCollectionDto(0, "", "",""),
    val budget: Int = 0,
    val genres: List<MovieGenreDto> = emptyList(),
    val homepage: String? = "",
    val id: Int = 0,
    val imdb_id: String? = "",
    val original_language: String = "",
    val original_title: String = "",
    val overview: String = "",
    val popularity: Float = 0f,
    val poster_path: String? = "",
    val production_companies: List<ProductionCompaniesDto> = emptyList(),
    val production_countries: List<ProductionCountriesDto> = emptyList(),
    val release_date: String = "",
    val revenue: Int = 0,
    val runtime: Int? = 0,
    val spoken_languages: List<SpokenLanguagesDto> = emptyList(),
    val status: String ="",
    val tagline: String? ="",
    val title: String="",
    val video: Boolean=false,
    val vote_average: Float=0f,
    val vote_count: Int=0,
    val credits: CreditDto = CreditDto(emptyList(), emptyList()),
    val images: MovieImagesDto = MovieImagesDto(emptyList(), emptyList(), emptyList()),
    val similar: MovieResponseDto = MovieResponseDto(0, emptyList(), 0, 0)
)