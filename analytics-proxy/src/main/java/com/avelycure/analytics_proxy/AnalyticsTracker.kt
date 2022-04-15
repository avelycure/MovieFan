package com.avelycure.analytics_proxy

interface AnalyticsTracker {
    fun trackEvent(eventName: String, params: Map<String, Any>? = null)
}