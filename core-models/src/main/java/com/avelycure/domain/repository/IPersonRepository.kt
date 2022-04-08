package com.avelycure.domain.repository

import com.avelycure.domain.models.Person
import com.avelycure.domain.models.PersonInfo

interface IPersonRepository {
    suspend fun getPopularPersons(page: Int): List<Person>
    suspend fun getPersonInfo(id: Int): PersonInfo
    suspend fun searchPerson(query: String, page: Int): List<Person>
}