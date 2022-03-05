package com.avelycure.domain.models

data class Person(
    val id: Int,
    val profilePath: String,
    val adult: Boolean,
    val name: String,
    val popularity: Float,
    val knownForDepartment: String,
    val knownForMovie: List<String>,
    val knownForTv: List<String>
) {
    var expanded: Boolean = false
    var birthday: String? = ""
    var deathDay: String? = ""
    var alsoKnownAs: List<String> = emptyList()
    var gender: Int = -1
    var biography: String = ""
    var placeOfBirth: String? = ""
    var imdbId: String = ""
    var homepage: String? = ""
    var profileImages: List<String> = emptyList()
}