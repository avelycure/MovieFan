package com.avelycure.movie_info.domain.interactors

import android.util.Log
import com.avelycure.domain.repository.IMovieInfoRepository
import com.avelycure.domain.repository.IMovieRepository
import kotlinx.coroutines.flow.flow

class GetMovieInfo(
    private val repository: IMovieInfoRepository,
    private val repo: IMovieRepository
) {

    fun execute(
        id: Int
    ) = flow{
        Log.d("mytag", "IMovieInfo: " + repository.hashCode())
        Log.d("mytag", "IMovie: " + repo.hashCode())
        emit(1)
    }

}