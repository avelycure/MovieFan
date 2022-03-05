package com.avelycure.data.remote.mappers

import com.avelycure.data.remote.dto.person.ResponsePersonInfo
import com.avelycure.domain.models.PersonInfo

fun ResponsePersonInfo.toPersonInfo(): PersonInfo {
    return PersonInfo(
        birthday = birthday,
        knownForDepartment = known_for_department,
        deathDay = deathday,
        id = id,
        name = name,
        alsoKnownAs = also_known_as,
        gender = gender,
        biography = biography,
        popularity = popularity,
        placeOfBirth = place_of_birth,
        profilePath = profile_path,
        adult = adult,
        imdbId = imdb_id,
        homepage = homepage,
        profileImages = images?.profiles?.map { it.file_path } ?: emptyList()
    )
}