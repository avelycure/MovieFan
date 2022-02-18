package com.avelycure.domain.repository

import com.avelycure.domain.models.Person

interface IPersonRepository {
    suspend fun getPopularPersons(page: Int): List<Person>
}