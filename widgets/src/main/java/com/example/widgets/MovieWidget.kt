package com.example.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.widget.RemoteViews

class MovieWidget: AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        val thisWidget = ComponentName(context, MovieWidget::class.java)
        val allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)

        for(widgetId in allWidgetIds){
            val remoteViews = RemoteViews(context.packageName, R.layout.widget_layout)
            appWidgetManager.updateAppWidget(widgetId, remoteViews)
        }
    }
}