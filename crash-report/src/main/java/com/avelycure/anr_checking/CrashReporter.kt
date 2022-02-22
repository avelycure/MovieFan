package com.avelycure.anr_checking

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import java.lang.Exception
import kotlin.concurrent.thread

const val PING_TIME: Long = 4000L

class CrashReporter(
    private val mainHandler: Handler,
    private val lifecycle: Lifecycle,
    private val context: Context
) : LifecycleObserver {
    private var tick1: Int = 0
    private var tick2: Int = 0
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var t: Thread

    fun registerObserver() {
        //FirebaseApp.initializeApp(context)
        analytics = FirebaseAnalytics.getInstance(context)
        pingMainLooper()
        lifecycle.addObserver(this)
    }

    /**
     * Main thread will live even if the app goes to the background.
     * The only reason to stop tracking is that the app was closed(ON_DESTROY method)
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun stopLogging() {
        lifecycle.removeObserver(this)
        t.interrupt()
    }

    private fun pingMainLooper() {
        t = Thread {
            while (true) {
                try {
                    mainHandler.post {
                        tick2++
                    }
                    Thread.sleep(PING_TIME)
                    if (tick1 != tick2)
                        tick1 = tick2
                    else {
                        Log.d("mytag", "ANR!!!")
                        val bundle = Bundle().apply {
                            putInt("a", 1)
                            putString("type", "ANR")
                            putString("description", "UI thread is not responding")
                        }
                        analytics.logEvent("ANR", bundle)
                    }
                } catch (e: Exception) {
                    if (e is InterruptedException)
                        Log.d("mytag", "Crash reporter was interrupted")
                }
            }
        }
        t.start()
    }

}