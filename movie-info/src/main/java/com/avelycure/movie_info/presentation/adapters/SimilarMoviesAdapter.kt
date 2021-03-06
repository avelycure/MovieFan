package com.avelycure.movie_info.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.avelycure.data.constants.RequestConstants
import com.avelycure.domain.models.Movie
import com.avelycure.movie_info.R

class SimilarMoviesAdapter(
    private val loadImage: (String, ImageView) -> Unit,
    private val openMovieInfo: (Int) -> Unit
) : RecyclerView.Adapter<SimilarMoviesAdapter.SimilarMoviesViewHolder>() {
    var similarMovies: List<Movie> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarMoviesViewHolder {
        return SimilarMoviesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_similar_movies, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SimilarMoviesViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = similarMovies.size

    inner class SimilarMoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: AppCompatImageView = itemView.findViewById(R.id.similar_movie_image_item)
        fun bind(pos: Int) {
            loadImage(
                RequestConstants.IMAGE + similarMovies[pos].posterPath,
                image
            )
            image.setOnClickListener {
                openMovieInfo(similarMovies[pos].movieId)
            }
        }
    }
}
