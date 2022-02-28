package com.avelycure.person.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.avelycure.core_navigation.IInstantiator

class PersonFragment:Fragment() {
    companion object Instantiator : IInstantiator {
        private const val tag = "PERSON"
        override fun getTag(): String {
            return tag
        }

        override fun getInstance(bundle: Bundle): Fragment {
            val movieInfoFragment = PersonFragment()
            movieInfoFragment.arguments = bundle
            return movieInfoFragment
        }
    }

}