package com.avelycure.analytics_proxy

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

class AnalyticsProxyInvocationHandler(
    private val analyticsTracker: AnalyticsTracker
) : InvocationHandler {
    @Suppress("NewApi")
    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any {
        if (method.parameterCount == 0) {
            val eventName = method.annotations.firstNotNullOf { it as? EventName }
            val analyticsEventName = eventName.value
            analyticsTracker.trackEvent(analyticsEventName)
        }
        return Unit
    }
}