package com.avelycure.image_loader.target

import android.graphics.Bitmap

interface CustomTarget {
    fun onResourceReady(bitmap: Bitmap)
}