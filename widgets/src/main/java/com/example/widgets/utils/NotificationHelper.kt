package com.example.widgets.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.widgets.R
import com.example.widgets.utils.NotificationConstants.CHANNEL_DESCRIPTION
import com.example.widgets.utils.NotificationConstants.CHANNEL_ID
import com.example.widgets.utils.NotificationConstants.CHANNEL_NAME
import com.example.widgets.utils.NotificationConstants.NOTIFICATION_ID

class NotificationHelper(private val context: Context) {
    private val notificationManager by lazy {
        context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
    }

    private val notificationBuilder: NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("MovieFan")
            .setSound(null)
            .setSmallIcon(R.drawable.artist_icon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
    }

    fun getNotification(): Notification {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notificationManager.createNotificationChannel(createChannel())
        }
        return notificationBuilder.build()
    }

    fun updateNotification(notificationText: String? = null){
        notificationText?.let {
            notificationBuilder.setContentText(it)
        }
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() =
        NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = CHANNEL_DESCRIPTION
            setSound(null,null)
        }
}