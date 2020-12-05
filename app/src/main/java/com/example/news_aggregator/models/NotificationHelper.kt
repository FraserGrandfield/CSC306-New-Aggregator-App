package com.example.news_aggregator.models

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import androidx.core.app.NotificationCompat
import com.example.news_aggregator.R

class NotificationHelper(base: Context?) : ContextWrapper(base) {
    private var channelID = "channelID"
    private var channelName = "channel name"

    private lateinit var notificationManager: NotificationManager

    fun createChannel() {
        val notificationChannel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)

        getManager().createNotificationChannel(notificationChannel)
    }

    fun getManager() : NotificationManager {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return notificationManager
    }

    fun getChannelNotification(title: String, text: String) : NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, channelID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_baseline_notifications_none_24)
    }
}