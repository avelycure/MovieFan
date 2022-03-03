package com.avelycure.person.presentation.adapters

import android.app.Person
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.avelycure.person.R

class PersonImagesAdapter() : RecyclerView.Adapter<PersonImagesAdapter.PersonImagesViewHolder>() {

    var data: List<String> = emptyList()

    inner class PersonImagesViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView)
    {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonImagesViewHolder {
        return PersonImagesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_person_images, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PersonImagesViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return data.size
    }
}
