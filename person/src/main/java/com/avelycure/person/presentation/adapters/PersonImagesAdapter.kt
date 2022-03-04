package com.avelycure.person.presentation.adapters

import android.app.Person
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.avelycure.data.constants.RequestConstants
import com.avelycure.person.R

class PersonImagesAdapter() : RecyclerView.Adapter<PersonImagesAdapter.PersonImagesViewHolder>() {

    var data: List<String> = emptyList()
    lateinit var loadImage: (String, ImageView) -> Unit

    inner class PersonImagesViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.p_iv_images)
        fun bind(item: String) {
            loadImage(RequestConstants.IMAGE + item, image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonImagesViewHolder {
        return PersonImagesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_person_images, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PersonImagesViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
