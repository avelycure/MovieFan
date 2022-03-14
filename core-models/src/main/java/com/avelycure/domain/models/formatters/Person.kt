package com.avelycure.domain.models.formatters

import com.avelycure.domain.models.Person

fun Person.getNiceMovies(): String {
    val movies = buildString {
        for (element in knownForMovie.take(5))
            append("$element, ")
    }
    return if (movies.isNotBlank() && movies.isNotEmpty())
        return movies.substring(0, movies.length - 2)
    else
        ""
}

fun Person.getNiceTvs(): String {
    val tvs = buildString {
        for (element in knownForTv.take(5))
            append("$element, ")
    }
    return if (tvs.isNotBlank() && tvs.isNotEmpty())
        return tvs.substring(0, tvs.length - 2)
    else
        ""
}