package com.example.widgets.movie

import android.app.PendingIntent
import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import com.avelycure.domain.models.formatters.getYear
import com.avelycure.domain.repository.IMovieRepository
import com.example.widgets.R
import com.example.widgets.utils.NotificationConstants.NOTIFICATION_ID
import com.example.widgets.utils.NotificationHelper
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
        startForeground(NOTIFICATION_ID, notificationHelper.getNotification())
    }

    @Inject
    lateinit var repo: IMovieRepository

    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val appWidgetManager = AppWidgetManager.getInstance(applicationContext)
        val allWidgetIds = intent?.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS)

        if (allWidgetIds != null) {

            for (widgetId in allWidgetIds) {
                serviceScope.launch {
                    val remoteViews =
                        RemoteViews(applicationContext.packageName, R.layout.movie_widget_layout)
                    val list = repo.getPopularMovies(1).take(3)

                    remoteViews.setTextViewText(
                        R.id.mwidget_tv1,
                        "1. ${list[0].title}, ${list[0].getYear()}"
                    )
                    remoteViews.setTextViewText(
                        R.id.mwidget_tv2,
                        "2. ${list[1].title}, ${list[1].getYear()}"
                    )
                    remoteViews.setTextViewText(
                        R.id.mwidget_tv3,
                        "3. ${list[2].title}, ${list[2].getYear()}"
                    )
                    appWidgetManager.updateAppWidget(widgetId, remoteViews)

                    try {
                        val intent = Intent("android.intent.action.MAIN")
                        intent.addCategory("android.intent.category.LAUNCHER")

                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.component = ComponentName(
                            "com.avelycure.moviefan",
                            "com.avelycure.moviefan.presentation.MainActivity"
                        )
                        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            PendingIntent.getActivity(
                                applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE
                            )
                        } else {
                            PendingIntent.getActivity(
                                applicationContext, 0, intent, 0
                            )
                        }
                        remoteViews.setOnClickPendingIntent(
                            R.id.mw_container, pendingIntent
                        )
                        appWidgetManager.updateAppWidget(widgetId, remoteViews)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(
                            applicationContext,
                            "There was a problem loading the application: ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    stopSelf(startId)
                }
            }
        }
        return START_STICKY
    }
}