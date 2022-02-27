package com.avelycure.image_loader

import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.widget.ImageView
import com.avelycure.image_loader.constants.ImageLoaderConstants.POOL_SIZE
import com.avelycure.image_loader.cache.Cacher
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ImageLoader(
    context: Context,
    placeholder: Int,
    threadsNum: Int = POOL_SIZE
) {
    private val executors: ExecutorService = Executors.newFixedThreadPool(threadsNum)
    private val handler: Handler = Handler(context.mainLooper)
    private val cacher: Cacher
    val defaultImage: Bitmap = BitmapFactory.decodeResource(context.resources, placeholder)

    init {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val maxKb = am.memoryClass * 1024
        val limitKb = maxKb / 8
        cacher = Cacher(limitKb)
    }

    private fun getBitmap(url: String): Bitmap {
        return if (cacher.getBitmapFromMemory(url) != null) {
            //the image can be lost after this check, so we just call this func again
            cacher.getBitmapFromMemory(url) ?: getBitmap(url)
        } else {
            val byteStream = URL(url).openStream()
            val image = BitmapFactory.decodeStream(byteStream)
            cacher.putBitmapInMemory(url, image)
            image
        }
    }

    fun loadImage(url: String, imageView: ImageView) {
        imageView.setImageBitmap(defaultImage)
        executors.execute {
            try {
                val image = getBitmap(url)
                handler.post {
                    imageView.setImageBitmap(image)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadImage(
        url: String,
        onDownloaded: (Bitmap) -> Unit,
    ) {
        executors.execute {
            try {
                val bmp = getBitmap(url)
                onDownloaded(bmp)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadImage(
        url: String,
        onDownloaded: (Bitmap) -> Unit,
        onPreload: (Bitmap) -> Unit
    ) {
        executors.execute {
            try {
                onPreload(defaultImage)
                val bmp = getBitmap(url)
                onDownloaded(bmp)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}