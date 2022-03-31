package com.avelycure.person.utils

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.avelycure.domain.models.Person

const val TAG = "PersonDiffutilsCallback"

class PersonDiffutilsCallback(
    private val oldList: List<Person>,
    private val newList: List<Person>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    var t = 0

    /**
     * This method is called first, we check if ids are not the same than it is different elements
     * If ids are the same then the personNew and personOld are the same elements but from different
     * list and we should call areContents the same
     */
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val personOld = oldList[oldItemPosition]
        val personNew = newList[newItemPosition]

        if (t == 0) {
            Log.d(TAG, "old: $oldList")
            Log.d(TAG, "new: $newList")
            t++
        }

        Log.d(TAG, "_1_${personNew.name}, ${personOld.name}: ${personNew.id == personOld.id}")
        return (personOld.id == personNew.id)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val personOld = oldList[oldItemPosition]
        val personNew = newList[newItemPosition]
        Log.d(
            TAG,
            "_2_${personNew.name} ${personNew.expanded}, ${personOld.name}  ${personOld.expanded}: ${personNew == personOld}"
        )
        return personNew == personOld
    }
}