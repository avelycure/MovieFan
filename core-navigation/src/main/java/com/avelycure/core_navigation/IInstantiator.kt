package com.avelycure.core_navigation

import android.os.Bundle
import androidx.fragment.app.Fragment

interface IInstantiator {
    fun getTag(): String
    fun getInstance(bundle: Bundle): Fragment
}