package com.avelycure.movie.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avelycure.core_navigation.Navigator
import com.avelycure.data.constants.RequestConstants
import com.avelycure.data.constants.TranslationConstants
import com.avelycure.domain.models.Movie
import com.avelycure.domain.models.formatters.getNiceGenres
import com.avelycure.domain.models.formatters.getOriginalTitleAndReleaseDate
import com.avelycure.movie.R

class HomeAdapter() : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    var data: List<Movie> = emptyList()
    var onClickedItem: (Int) -> Unit = {_->}
    var fetchMore: () -> Unit = {}
    var loadImages: (String, ImageView) -> Unit = {_,_->}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie, parent, false)
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(item = data[position], onClicked = onClickedItem)
    }

    inner class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTitle = view.findViewById<AppCompatTextView>(R.id.pmi_title)
        private val movieLogo = view.findViewById<AppCompatImageView>(R.id.pmi_iv)
        private val tvReviews = view.findViewById<AppCompatTextView>(R.id.pmi_tv_reviews)
        private val ratingBar = view.findViewById<AppCompatRatingBar>(R.id.pmi_rating_bar)
        private val tvGenres = view.findViewById<AppCompatTextView>(R.id.pmi_tv_genres)
        private val tvOriginalTitle =
            view.findViewById<AppCompatTextView>(R.id.pmi_original_title)

        fun bind(item: Movie?, onClicked: (Int) -> Unit) {
            item?.let { popularMovie ->
                tvTitle.text = popularMovie.title
                tvReviews.text = popularMovie.voteCount.toString()
                ratingBar.rating = popularMovie.voteAverage / 2F
                tvOriginalTitle.text = popularMovie.getOriginalTitleAndReleaseDate()
                tvGenres.text = popularMovie.getNiceGenres()

                loadImages(
                    RequestConstants.IMAGE + popularMovie.posterPath,
                    movieLogo
                )
            }

            itemView.setOnClickListener {
                onClicked(item!!.movieId)
            }
        }
    }

    var loading: Boolean = false
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        val llm = manager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visiblePosition = llm.findLastCompletelyVisibleItemPosition()
                if (!loading && visiblePosition > itemCount - 10) {
                    fetchMore()
                    loading = true
                }
            }
        })
    }
}