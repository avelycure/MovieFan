package com.avelycure.data.remote.dto.person
import kotlinx.serialization.Serializable

@Serializable
data class ResponsePerson(
    val page: Int = 0,
    val results: List<ResultPerson> = emptyList(),
    val total_results: Int = 0,
    val total_pages: Int = 0
)