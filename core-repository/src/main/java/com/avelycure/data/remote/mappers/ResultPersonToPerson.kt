package com.avelycure.data.remote.mappers

import com.avelycure.data.remote.dto.person.KnownForMovie
import com.avelycure.data.remote.dto.person.KnownForTv
import com.avelycure.data.remote.dto.person.ResultPerson
import com.avelycure.domain.models.Person

fun ResultPerson.toPerson(): Person {
    val knownForMovie = mutableListOf<KnownForMovie>()
    val knownForTv = mutableListOf<KnownForTv>()

    for (media in known_for) {
        if (media is KnownForMovie)
            knownForMovie.add(media)
        if (media is KnownForTv)
            knownForTv.add(media)
    }

    return Person(
        profilePath = profile_path ?: "",
        adult = adult,
        id = id,
        knownForMovie = knownForMovie.map { it.title },
        knownForTv = knownForTv.map { it.name },
        name = name,
        popularity = popularity,
        knownForDepartment = known_for_department ?: ""
    )
}