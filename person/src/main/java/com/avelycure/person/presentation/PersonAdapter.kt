package com.avelycure.person.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.avelycure.domain.models.Person
import com.avelycure.person.R

class PersonAdapter: RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {
    var data: List<Person> = emptyList()

    var onClickedItem: (Int) -> Unit = {}

    inner class PersonViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val tvName: AppCompatTextView = view.findViewById(R.id.pi_primary_person_name)
        private val ivPoster: AppCompatImageView = view.findViewById(R.id.pi_primary_iv)
        private val tvBiography: AppCompatTextView = view.findViewById(R.id.pia_biography)
        private val expLayout: ConstraintLayout = view.findViewById(R.id.pi_additional_layout)
        private val layout: ConstraintLayout = view.findViewById(R.id.pi_layout)
        private val tvs: AppCompatTextView = view.findViewById(R.id.pi_primary_tvlist)
        private val tvsTitle: AppCompatTextView = view.findViewById(R.id.pi_primary_tv_title)
        private val movies: AppCompatTextView = view.findViewById(R.id.pi_primary_movieslist)
        private val moviesTitle: AppCompatTextView = view.findViewById(R.id.pi_primary_movies_titles)
        private val tvDeathday: AppCompatTextView = view.findViewById(R.id.pia_deathday)
        private val tvDeathdayTitle: AppCompatTextView =
            view.findViewById(R.id.pia_deathday_title)
        private val tvAlsoKnownAs: AppCompatTextView =
            view.findViewById(R.id.pia_also_known_as)
        private val tvAlsoKnownAsTitle: AppCompatTextView =
            view.findViewById(R.id.pia_also_known_as_title)
        private val tvBiographyTitle: AppCompatTextView = view.findViewById(R.id.pia_biography_title)

        private val tvHomepage: AppCompatTextView = view.findViewById(R.id.pia_homepage)
        private val tvHomepageTitle: AppCompatTextView =
            view.findViewById(R.id.pia_homepage_title)
        private val tvPlaceOfBirth: AppCompatTextView =
            view.findViewById(R.id.pia_place_of_birth)
        private val tvPlaceOfBirthTitle: AppCompatTextView =
            view.findViewById(R.id.pia_place_of_birth_title)
        private val tvDateOfBirth: AppCompatTextView = view.findViewById(R.id.pia_birthday)
        private val tvDateOfBirthTitle: AppCompatTextView =
            view.findViewById(R.id.pia_birthday_title)

        private val rvPersonImages: RecyclerView = view.findViewById(R.id.pia_rv_photos)
        private val tvDepartment: AppCompatTextView = view.findViewById(R.id.pi_primary_department)

        fun bind(item: Person?, onClicked: (Int) -> Unit) {
            item?.let { person ->
                tvName.text = person.name
                tvDepartment.text = person.knownForDepartment
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        return PersonViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_person, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.bind(item = data[position], onClicked = onClickedItem)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}