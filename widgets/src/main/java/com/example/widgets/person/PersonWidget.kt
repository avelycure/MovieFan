package com.example.widgets.person

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.widgets.movie.MovieWidget

class PersonWidget: AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        val thisWidget = ComponentName(
            context,
            PersonWidget::class.java
        )
        val allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)

        val intent = Intent(
            context.applicationContext,
            PersonWidgetUpdateService::class.java
        )
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        }
    }
}