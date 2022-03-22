package com.avelycure.domain.models.formatters

import com.avelycure.domain.models.Movie

fun Movie.getNiceGenres(): String {
    val companies = buildString {
        for (element in genres) {
            append("$element, ")
        }
    }
    return if (companies.isNotBlank() && companies.isNotEmpty())
        return companies.substring(0, companies.length - 2)
    else
        ""
}

fun Movie.getOriginalTitleAndReleaseDate(): String =
    if (this.releaseDate.isNotEmpty())
        originalTitle + ", " + this.releaseDate.substring(0, 4)
    else
        originalTitle

fun Movie.getYear(): String = this.releaseDate.substring(0, 4)