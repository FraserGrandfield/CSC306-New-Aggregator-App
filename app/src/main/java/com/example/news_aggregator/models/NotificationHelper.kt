package com.example.news_aggregator.models

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.news_aggregator.R
import com.example.news_aggregator.activities.ArticleActivity

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

    fun getChannelNotification(title: String, summary: String, author: String, publisher: String, url: String, image: String) : NotificationCompat.Builder {
        val intent = Intent(this, ArticleActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("title", title)
            putExtra("summary", summary)
            putExtra("publisher", publisher)
            putExtra("author", author)
            putExtra("image", image)
            putExtra("url", url)
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        return NotificationCompat.Builder(applicationContext, channelID)
            .setStyle(NotificationCompat.BigTextStyle())
            .setContentTitle(title)
            .setContentText(summary)
            .setSmallIcon(R.drawable.ic_baseline_notifications_none_24)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
    }
}