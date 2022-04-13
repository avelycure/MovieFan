package com.avelycure.moviefan.analytics

import com.avelycure.analytics_proxy.EventName
import com.avelycure.analytics_proxy.Param

interface AppAnalytics {
    @EventName("appStart")
    fun trackAppStart()

    @EventName("ANR")
    fun trackANR()
}