package com.avelycure.domain.models.formatters

import com.avelycure.domain.models.MovieInfo

fun MovieInfo.getNiceGenres(): String {
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

fun MovieInfo.getNiceCompanies(): String {
    val companies = buildString {
        for (element in productionCompanies) {
            append("$element, ")
        }
    }
    return if (companies.isNotBlank() && companies.isNotEmpty())
        return companies.substring(0, companies.length - 2)
    else
        ""
}

fun MovieInfo.getNiceCountries(): String {
    val countries = buildString {
        for (element in productionCountries)
            append("$element, ")
    }
    return if (countries.isNotBlank() && countries.isNotEmpty())
        countries.substring(0, countries.length - 2)
    else
        ""
}

fun MovieInfo.getNiceCast(): String {
    val cast = buildString {
        for (element in cast.take(5))
            append("$element, ")
    }
    return if (cast.isNotBlank() && cast.isNotEmpty())
        return cast.substring(0, cast.length - 2)
    else
        ""
}