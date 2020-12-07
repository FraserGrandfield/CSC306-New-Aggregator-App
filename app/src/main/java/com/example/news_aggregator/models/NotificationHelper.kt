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
    private var channelID = base?.getString(R.string.notification_channel_id)
    private var channelName = base?.getString(R.string.notification_channel_name)

    private lateinit var notificationManager: NotificationManager

    fun createChannel() {
        val notificationChannel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)

        getManager().createNotificationChannel(notificationChannel)
    }

    fun getManager() : NotificationManager {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return notificationManager
    }

    fun getChannelNotification(title: String, summary: String, author: String, publisher: String, url: String, image: String, context: Context) : NotificationCompat.Builder? {
        val intent = Intent(this, ArticleActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(context.getString(R.string.article_data_title), title)
            putExtra(context.getString(R.string.article_data_summary), summary)
            putExtra(context.getString(R.string.article_data_publisher), publisher)
            putExtra(context.getString(R.string.article_data_author), author)
            putExtra(context.getString(R.string.article_data_image), image)
            putExtra(context.getString(R.string.article_data_article_url), url)
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        return channelID?.let {
            NotificationCompat.Builder(applicationContext, it)
                .setStyle(NotificationCompat.BigTextStyle())
                .setContentTitle(title)
                .setContentText(summary)
                .setSmallIcon(R.drawable.ic_baseline_notifications_none_24)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        }
    }
}