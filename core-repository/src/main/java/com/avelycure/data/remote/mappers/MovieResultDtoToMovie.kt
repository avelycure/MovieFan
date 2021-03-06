package com.avelycure.data.remote.mappers

import com.avelycure.data.constants.TranslationConstants
import com.avelycure.data.remote.dto.movie.MovieResultDto
import com.avelycure.domain.models.Movie

fun MovieResultDto.toMovie(): Movie {
    return Movie(
        title = title,
        originalTitle = original_title,
        posterPath = poster_path ?: "",
        genres = genre_ids.map {
            TranslationConstants.movieGenre[it] + " "
        },
        popularity = popularity,
        voteAverage = vote_average,
        releaseDate = release_date,
        movieId = id,
        voteCount = vote_count
    )
}