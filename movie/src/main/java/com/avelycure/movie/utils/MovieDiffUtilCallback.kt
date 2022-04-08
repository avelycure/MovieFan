package com.avelycure.movie.utils

import androidx.recyclerview.widget.DiffUtil
import com.avelycure.domain.models.Movie

class MovieDiffUtilCallback(
    private val oldList: List<Movie>,
    private val newList: List<Movie>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val movieOld = oldList[oldItemPosition]
        val movieNew = newList[newItemPosition]
        return (movieOld.movieId == movieNew.movieId)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val movieOld = oldList[oldItemPosition]
        val movieNew = newList[newItemPosition]
        return movieOld == movieNew
    }
}