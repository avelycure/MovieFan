package com.avelycure.movie_info.domain.mappers

import com.avelycure.domain.models.MovieInfo

fun MovieInfo.getGenresString(): String {
    val genresString = buildString {
        for (genre in genres)
            append("$genre, ")
    }
    return if (genresString.isNotBlank() && genresString.isNotEmpty() && genresString.length > 1)
        return genresString.substring(0, genresString.length - 2)
    else
        ""
}

fun MovieInfo.getCompaniesString(): String {
    val companies = buildString {
        for (company in productionCompanies) {
            append("$company, ")
        }
    }
    return if (companies.isNotBlank() && companies.isNotEmpty() && companies.length > 1)
        return companies.substring(0, companies.length - 2)
    else
        ""
}

fun MovieInfo.getCountriesString(): String {
    val countries = buildString {
        for (country in productionCountries)
            append("$country, ")
    }
    return if (countries.isNotBlank() && countries.isNotEmpty() && countries.length > 1)
        countries.substring(0, countries.length - 2)
    else
        ""
}

fun MovieInfo.getCastString(): String {
    val castString = buildString {
        for (element in cast)
            append("$element, ")
    }
    return if (castString.isNotBlank() && castString.isNotEmpty() && castString.length > 1)
        return castString.substring(0, castString.length - 2)
    else
        ""
}