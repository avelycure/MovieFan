package com.example.office.presentation

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.avelycure.core_navigation.IInstantiator
import com.example.office.R

class OfficeFragment: Fragment(){

    companion object Instantiator: IInstantiator{
        private const val tag = "OfficeFragment"

        override fun getTag(): String {
            return tag
        }

        override fun getInstance(bundle: Bundle): Fragment {
            val officeFragment = OfficeFragment()
            officeFragment.arguments = bundle
            return officeFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.office_fragment, container, false)
        return view
    }


}