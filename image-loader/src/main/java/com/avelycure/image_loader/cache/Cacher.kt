package com.avelycure.image_loader.cache

import android.graphics.Bitmap
import android.util.Log
import android.util.LruCache

internal class Cacher(limitKb: Int) : LruCache<String, Bitmap>(limitKb) {
    fun getBitmapFromMemory(url: String): Bitmap? {
        return this.get(url)
    }

    fun putBitmapInMemory(url: String, bmp: Bitmap) {
        if (getBitmapFromMemory(url) == null) {
            this.put(url, bmp)
            //Log.d("mytag", "added to cache:${url}")
        }
    }
}