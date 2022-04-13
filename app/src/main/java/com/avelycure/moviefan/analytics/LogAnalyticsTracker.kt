package com.avelycure.moviefan.analytics

import android.util.Log
import com.avelycure.analytics_proxy.AnalyticsTracker

class LogAnalyticsTracker: AnalyticsTracker {
    override fun trackEvent(eventName: String, params: Map<String, Any>?) {
        Log.d("mytag", "Log analytics")
        Log.d("mytag", "$eventName($params)")
    }
}