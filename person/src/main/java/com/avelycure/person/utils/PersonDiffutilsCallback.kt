package com.avelycure.person.utils

import androidx.recyclerview.widget.DiffUtil
import com.avelycure.domain.models.Person

class PersonDiffutilsCallback(
    private val oldList: List<Person>,
    private val newList: List<Person>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    /**
     * This method is called first, we check if ids are not the same then it is different elements
     * If ids are the same then the personNew and personOld are the same elements but from different
     * list and we should call areContents the same
     */
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val personOld = oldList[oldItemPosition]
        val personNew = newList[newItemPosition]
        return (personOld.id == personNew.id)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val personOld = oldList[oldItemPosition]
        val personNew = newList[newItemPosition]
        return personNew == personOld
    }
}