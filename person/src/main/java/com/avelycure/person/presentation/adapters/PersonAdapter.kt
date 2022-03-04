package com.avelycure.person.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avelycure.data.constants.RequestConstants
import com.avelycure.domain.models.Person
import com.avelycure.domain.models.PersonInfo
import com.avelycure.person.R
import com.avelycure.person.utils.setProperties
import com.avelycure.person.utils.showIfNotBlank
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PersonAdapter : RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {
    var data: List<Person> = emptyList()

    lateinit var onExpand: (personId: Int, itemId: Int) -> Unit

    var loadImage: (path: String, iv: ImageView) -> Unit = { _, _ -> }


    lateinit var scope: LifecycleCoroutineScope

    inner class PersonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val personImagesAdapter: PersonImagesAdapter = PersonImagesAdapter()

        private val tvName: AppCompatTextView = view.findViewById(R.id.pi_primary_person_name)
        private val ivPoster: AppCompatImageView = view.findViewById(R.id.pi_primary_iv)
        private val tvBiography: AppCompatTextView = view.findViewById(R.id.pia_biography)
        private val expLayout: ConstraintLayout = view.findViewById(R.id.pi_additional_layout)
        private val layout: ConstraintLayout = view.findViewById(R.id.pi_layout)
        private val tvs: AppCompatTextView = view.findViewById(R.id.pi_primary_tvlist)
        private val tvsTitle: AppCompatTextView = view.findViewById(R.id.pi_primary_tv_title)
        private val movies: AppCompatTextView = view.findViewById(R.id.pi_primary_movieslist)
        private val moviesTitle: AppCompatTextView =
            view.findViewById(R.id.pi_primary_movies_titles)
        private val tvDeathday: AppCompatTextView = view.findViewById(R.id.pia_deathday)
        private val tvDeathdayTitle: AppCompatTextView =
            view.findViewById(R.id.pia_deathday_title)
        private val tvAlsoKnownAs: AppCompatTextView =
            view.findViewById(R.id.pia_also_known_as)
        private val tvAlsoKnownAsTitle: AppCompatTextView =
            view.findViewById(R.id.pia_also_known_as_title)
        private val tvBiographyTitle: AppCompatTextView =
            view.findViewById(R.id.pia_biography_title)

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

        fun bind(item: Person?, position: Int) {
            item?.let { person ->
                tvName.text = person.name
                showIfNotBlank(tvsTitle, tvs, person.knownForTv.toString())
                showIfNotBlank(moviesTitle, movies, person.knownForMovie.toString())
                tvDepartment.text = person.knownForDepartment

                if (person.expanded) {
                    expLayout.visibility = View.VISIBLE
                    showIfNotBlank(tvDateOfBirthTitle, tvDateOfBirth, person.birthday)
                    showIfNotBlank(tvPlaceOfBirthTitle, tvPlaceOfBirth, person.placeOfBirth)
                    showIfNotBlank(tvDeathdayTitle, tvDeathday, person.deathDay)
                    showIfNotBlank(tvAlsoKnownAsTitle, tvAlsoKnownAs, person.alsoKnownAs.toString())
                    showIfNotBlank(tvBiographyTitle, tvBiography, person.biography)
                    showIfNotBlank(tvHomepageTitle, tvHomepage, person.homepage)
                } else {
                    expLayout.visibility = View.GONE
                }
                personImagesAdapter.data = person.profileImages
                rvPersonImages.adapter = personImagesAdapter
                personImagesAdapter.loadImage = loadImage
                rvPersonImages.layoutManager =
                    LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

                layout.setOnClickListener {
                    person.expanded = !person.expanded

                    onExpand(person.id, position)
                }

                loadImage(
                    RequestConstants.IMAGE + person.profilePath,
                    ivPoster
                )
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
        holder.bind(item = data[position], position)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}