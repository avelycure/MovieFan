package com.avelycure.data.remote.mappers

import com.avelycure.data.remote.dto.movie.MovieResult
import com.avelycure.domain.models.Movie

fun MovieResult.toMovie(): Movie {
    return Movie(
        title = title,
        originalTitle = original_title,
        posterPath = poster_path,
        genreIds = genre_ids,
        popularity = popularity,
        voteAverage = vote_average,
        releaseDate = release_date,
        movieId = id,
        voteCount = vote_count
    )
}