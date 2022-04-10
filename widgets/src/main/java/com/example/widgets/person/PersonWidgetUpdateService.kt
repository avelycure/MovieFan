package com.example.widgets.person

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.IBinder
import android.widget.RemoteViews
import com.avelycure.domain.repository.IPersonRepository
import com.example.widgets.R
import com.example.widgets.utils.NotificationConstants.NOTIFICATION_ID
import com.example.widgets.utils.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class PersonWidgetUpdateService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @Inject
    lateinit var repo: IPersonRepository

    private lateinit var notificationHelper: NotificationHelper

    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())

    override fun onCreate() {
        super.onCreate()
        notificationHelper = NotificationHelper(applicationContext)
        startForeground(NOTIFICATION_ID, notificationHelper.getNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val appWidgetManager = AppWidgetManager.getInstance(applicationContext)
        val allWidgetIds = intent?.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS)

        if (allWidgetIds != null) {
            for (widgetId in allWidgetIds) {
                serviceScope.launch {
                    val remoteViews =
                        RemoteViews(applicationContext.packageName, R.layout.person_widget_layout)
                    val list = repo.getPopularPersons(1).take(3)

                    remoteViews.setTextViewText(
                        R.id.pwidget_tv1,
                        "1. ${list[0].name}, ${list[0].knownForDepartment}"
                    )
                    remoteViews.setTextViewText(
                        R.id.pwidget_tv2,
                        "2. ${list[1].name}, ${list[1].knownForDepartment}"
                    )
                    remoteViews.setTextViewText(
                        R.id.pwidget_tv3,
                        "3. ${list[2].name}, ${list[2].knownForDepartment}"
                    )
                    appWidgetManager.updateAppWidget(widgetId, remoteViews)

                    stopSelf(startId)
                }
            }
        }
        return START_STICKY
    }
}