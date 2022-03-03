package com.avelycure.person.utils

import android.view.View
import android.widget.TextView

fun showIfNotBlank(tvTitle: TextView,tvData: TextView, data: String?){
    if(data!=null){
        if(data.isBlank()){
            tvTitle.visibility = View.GONE
            tvData.visibility = View.GONE
        }else{
            tvData.visibility = View.VISIBLE
            tvTitle.visibility = View.VISIBLE
            tvData.text = data
        }
    }
}