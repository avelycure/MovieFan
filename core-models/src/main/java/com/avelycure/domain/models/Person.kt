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


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Person

        if (id != other.id) return false
        if (profilePath != other.profilePath) return false
        if (adult != other.adult) return false
        if (name != other.name) return false
        if (popularity != other.popularity) return false
        if (knownForDepartment != other.knownForDepartment) return false
        if (knownForMovie != other.knownForMovie) return false
        if (knownForTv != other.knownForTv) return false
        if (expanded != other.expanded) return false
        if (birthday != other.birthday) return false
        if (deathDay != other.deathDay) return false
        if (alsoKnownAs != other.alsoKnownAs) return false
        if (gender != other.gender) return false
        if (biography != other.biography) return false
        if (placeOfBirth != other.placeOfBirth) return false
        if (imdbId != other.imdbId) return false
        if (homepage != other.homepage) return false
        if (profileImages != other.profileImages) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + profilePath.hashCode()
        result = 31 * result + adult.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + popularity.hashCode()
        result = 31 * result + knownForDepartment.hashCode()
        result = 31 * result + knownForMovie.hashCode()
        result = 31 * result + knownForTv.hashCode()
        result = 31 * result + expanded.hashCode()
        result = 31 * result + (birthday?.hashCode() ?: 0)
        result = 31 * result + (deathDay?.hashCode() ?: 0)
        result = 31 * result + alsoKnownAs.hashCode()
        result = 31 * result + gender
        result = 31 * result + biography.hashCode()
        result = 31 * result + (placeOfBirth?.hashCode() ?: 0)
        result = 31 * result + imdbId.hashCode()
        result = 31 * result + (homepage?.hashCode() ?: 0)
        result = 31 * result + profileImages.hashCode()
        return result
    }

    override fun toString(): String {
        return "Person(name='$name', expanded=$expanded)"
    }


}