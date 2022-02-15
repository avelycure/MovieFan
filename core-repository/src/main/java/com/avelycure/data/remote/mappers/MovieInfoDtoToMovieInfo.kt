package com.avelycure.data.remote.mappers

import com.avelycure.data.remote.dto.movie_info.MovieInfoDto
import com.avelycure.domain.models.*

fun MovieInfoDto.toMovieInfo(): MovieInfo {
    return MovieInfo(
        adult = adult,
        budget = budget,
        imdbId = imdb_id ?: "",
        originalLanguage = original_language,
        originalTitle = original_title,
        overview = overview,
        popularity = popularity,
        genres = genres.map { it.name },
        productionCompanies = production_companies.map { it.name },
        productionCountries = production_countries.map { it.name },
        releaseDate = release_date,
        status = status,
        revenue = revenue,
        tagline = tagline ?: "",
        voteAverage = vote_average / 2F,
        title = title,
        voteCount = vote_count,
        posterPath = poster_path ?: "",
        cast = credits.cast.map {
            it.name
        },
        movieId = id,
        imagesBackdrop = images.backdrops.map {
            it.file_path
        },
        imagesPosters = images.posters.map {
            it.file_path
        },
        similar = similar.results.map {
            it.toMovie()
        }
    )
}