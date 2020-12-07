package com.example.news_aggregator.receivers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.news_aggregator.R
import com.example.news_aggregator.utils.NewsAPI
import com.example.news_aggregator.utils.NotificationHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Notification broadcast receiver for displaying notifications.
 * @property context Context
 * @property mAuth FirebaseAuth
 * @property database FirebaseFirestore
 */
class NotificationReceiver : BroadcastReceiver() {
    private lateinit var context: Context
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseFirestore

    /**
     * Runs when the receiver starts.
     * @param context Context
     * @param intent Intent
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            this.context = context
        }
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        getDurationTillNextNotification()
        getKeyTerms()
    }

    /**
     * Gets the latest article for each key term and displays as notification.
     * @param list ArrayList<String> list of key terms.
     */
    private fun getLatestArticle(list: ArrayList<String>) {
        for (i in list.indices)
            NewsAPI.getArticles(
                context.getString(R.string.news_api_top_headlines),
                context.getString(R.string.news_api_q),
                list[i],
                context.getString(R.string.news_api_published_at),
                true,
                context
            ) { articlesList ->
                if (articlesList.size > 0) {
                    val data = articlesList[0]
                    val notification = NotificationHelper(context)
                    notification.createChannel()
                    val notificationBuilder = notification.getChannelNotification(
                        data.title,
                        data.summary,
                        data.author,
                        data.publisher,
                        data.articleURL,
                        data.image,
                        context
                    )
                    if (notificationBuilder != null) {
                        notification.getManager().notify((i + 1), notificationBuilder.build())
                    }
                }
            }
    }

    /**
     * Gets the key terms of a user.
     */
    private fun getKeyTerms() {
        val list = ArrayList<String>()
        val ref = database.collection(context.getString(R.string.firestore_users))
            .document(mAuth.uid.toString())
        ref.get().addOnSuccessListener { snapshot ->
            if (snapshot != null) {
                list.clear()
                if (snapshot.data != null) {
                    val keyTerms =
                        snapshot.data?.get(context.getString(R.string.firestore_key_terms)) as ArrayList<*>
                    for (term in keyTerms) {
                        list.add(term.toString())
                    }
                }
                getLatestArticle(list)
            } else {
                Log.d("Error", "Current data: null")
            }
        }.addOnFailureListener {
            Log.e("NotificationReceiverError", "Could not get key terms")
        }
    }

    /**
     * Gets how long the next notification should be.
     */
    private fun getDurationTillNextNotification() {
        val ref = database.collection(context.getString(R.string.firestore_users))
            .document(mAuth.uid.toString())
        ref.get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    when (snapshot.get(context.getString(R.string.firestore_duration)) as Long) {
                        6L -> {
                            val time: Long = 60000 * 60 * 6
                            scheduleNextNotification(time)
                        }
                        12L -> {
                            val time: Long = 60000 * 60 * 12
                            scheduleNextNotification(time)
                        }
                        24L -> {
                            val time: Long = 60000 * 60 * 24
                            scheduleNextNotification(time)
                        }
                    }
                }

            }
            .addOnFailureListener {
                Log.e("NotificationReceiverError", "Could not get duration")
            }
    }

    /**
     * Schedules the next notification.
     * @param time Long time till next notification.
     */
    private fun scheduleNextNotification(time: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val newIntent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 1, newIntent, 0)
        val alarmTime = System.currentTimeMillis() + time
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
    }
}