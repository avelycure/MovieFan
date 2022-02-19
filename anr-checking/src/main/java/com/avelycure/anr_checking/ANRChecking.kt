package com.avelycure.anr_checking

import android.os.Handler
import android.util.Log
import kotlin.concurrent.thread

const val PING_TIME: Long = 4000L

class ANRChecking(
    private val mainHandler: Handler
) {
    private var tick1: Int = 0
    private var tick2: Int = 0

    fun pingMainLooper() {
        thread {
            while (true) {
                mainHandler.post {
                    tick2++
                }
                Thread.sleep(PING_TIME)
                if (tick1 != tick2)
                    tick1 = tick2
                else {
                    Log.d("mytag", "ANR!!!")
                    //logging logic
                }
            }
        }
    }

}