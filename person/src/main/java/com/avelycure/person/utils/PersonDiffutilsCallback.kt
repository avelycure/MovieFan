package com.avelycure.person.utils

import androidx.recyclerview.widget.DiffUtil
import com.avelycure.domain.models.Person

class PersonDiffutilsCallback(
    private val oldList: List<Person>,
    private val newList: List<Person>
) : DiffUtil.Callback() {


    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val personOld = oldList[oldItemPosition]
        val personNew = newList[newItemPosition]
        return personOld.id == personNew.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val personOld = oldList[oldItemPosition]
        val personNew = newList[newItemPosition]
        return personNew == personOld
    }
}