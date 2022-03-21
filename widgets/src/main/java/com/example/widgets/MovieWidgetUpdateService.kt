package com.example.widgets

import android.app.Notification
import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import com.avelycure.domain.models.formatters.getOriginalTitleAndReleaseDate
import com.avelycure.domain.repository.IMovieRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MovieWidgetUpdateService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private lateinit var notificationHelper: NotificationHelper

    override fun onCreate() {
        super.onCreate()
        notificationHelper = NotificationHelper(applicationContext)
        startForeground(notificationHelper.NOTIFICATION_ID, notificationHelper.getNotification())
    }

    @Inject
    lateinit var repo: IMovieRepository

    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("mytag", "command started")

        val appWidgetManager = AppWidgetManager.getInstance(applicationContext)
        val allWidgetIds = intent?.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS)

        if (allWidgetIds != null) {
            val lastWidgetId = allWidgetIds[allWidgetIds.size - 1]

            for (widgetId in allWidgetIds) {
                serviceScope.launch {
                    val remoteViews =
                        RemoteViews(applicationContext.packageName, R.layout.widget_layout)
                    val list = repo.getPopularMovies(1).take(3)

                    remoteViews.setTextViewText(
                        R.id.mwidget_tv1,
                        "${list[0].title}(${list[0].getOriginalTitleAndReleaseDate()})"
                    )
                    remoteViews.setTextViewText(
                        R.id.mwidget_tv2,
                        "${list[1].title}(${list[1].getOriginalTitleAndReleaseDate()})"
                    )
                    remoteViews.setTextViewText(
                        R.id.mwidget_tv3,
                        "${list[2].title}(${list[2].getOriginalTitleAndReleaseDate()})"
                    )
                    appWidgetManager.updateAppWidget(widgetId, remoteViews)

                    // this will cause bug in multithreading
                    // (widgetId == lastWidgetId)
                    //    stopSelf()
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }
}