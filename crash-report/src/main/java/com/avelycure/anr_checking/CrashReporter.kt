package com.avelycure.anr_checking

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import com.google.firebase.analytics.FirebaseAnalytics
import java.lang.Exception

const val PING_TIME: Long = 4000L
const val TAG = "CrashReporter"

/**
 * This class put small message in message queue and then waits for some time
 * If the messages was handled -> then its okay, main thread is not blocked
 * But if not -> ANR
 */
class CrashReporter(
    private val mainHandler: Handler,
    private val context: Context
) : LifecycleObserver {
    private var tick1: Int = 0
    private var tick2: Int = 0
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var t: Thread

    fun registerObserver() {
        analytics = FirebaseAnalytics.getInstance(context)
        pingMainLooper()
    }

    /**
     * Post to main looper to increase tick2, if after some time tick1==tick2 then
     * main thread is blocked
     */
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
                        Log.d(TAG, "ANR!")
                        val bundle = Bundle().apply {
                            putInt("a", 1)
                            putString("type", "ANR")
                            putString("description", "UI thread is not responding")
                        }
                        analytics.logEvent("ANR", bundle)
                    }
                } catch (e: Exception) {
                    if (e is InterruptedException)
                        Log.d(TAG, "Crash reporter was interrupted")
                }
            }
        }
        t.start()
    }

}