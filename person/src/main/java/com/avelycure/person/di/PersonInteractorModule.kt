package com.avelycure.person.di

import com.avelycure.domain.repository.IPersonRepository
import com.avelycure.person.domain.interactors.GetPersonInfo
import com.avelycure.person.domain.interactors.GetPopularPersons
import com.avelycure.person.domain.interactors.SearchPerson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersonInteractorModule {

    @Provides
    @Singleton
    fun provideGetPopularPerson(repository: IPersonRepository): GetPopularPersons{
        return GetPopularPersons(repository)
    }

    @Provides
    @Singleton
    fun provideGetPersonInfo(repository: IPersonRepository): GetPersonInfo{
        return GetPersonInfo(repository)
    }

    @Provides
    @Singleton
    fun provideSearchPerson(repository: IPersonRepository): SearchPerson{
        return SearchPerson(repository)
    }

}