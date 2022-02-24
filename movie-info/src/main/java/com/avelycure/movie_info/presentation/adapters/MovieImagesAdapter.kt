package com.avelycure.movie_info.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.avelycure.data.constants.RequestConstants
import com.avelycure.image_loader.ImageLoader
import com.avelycure.movie_info.R

class MovieImagesAdapter(
    private val imageLoader: ImageLoader
): RecyclerView.Adapter<MovieImagesAdapter.MovieImagesViewHolder>() {
    var imagesList: List<String> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieImagesViewHolder {
        return MovieImagesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_images, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieImagesViewHolder, position: Int) {
        imageLoader.loadImage(
            RequestConstants.IMAGE + imagesList[position],
            holder.image
        )
    }

    override fun getItemCount() = imagesList.size

    class MovieImagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: AppCompatImageView = itemView.findViewById(R.id.movie_item_image)
    }
}