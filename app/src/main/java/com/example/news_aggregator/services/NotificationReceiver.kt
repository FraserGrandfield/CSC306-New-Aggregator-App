 package com.example.news_aggregator.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.news_aggregator.R
import com.example.news_aggregator.models.NewsAPI
import com.example.news_aggregator.models.NotificationHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

 class NotificationReceiver : BroadcastReceiver() {
    private lateinit var context: Context
    private lateinit var mAuth: FirebaseAuth
     private lateinit var database: FirebaseFirestore

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            this.context = context
        }
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        getDurationTillNextNotification()
        getKeyTerms()
    }

     private fun getLatestArticle(list: ArrayList<String>) {
         for (i in list.indices)
         NewsAPI.getArticles("top-headlines", "q", list[i], "publishedAt", true) { list ->
             if (list.size > 0) {
                 val data = list[0]
                 val notification = NotificationHelper(context)
                 notification.createChannel()
                 val notificationBuilder = notification.getChannelNotification(data.title, data.summary, data.author, data.publisher, data.articleURL, data.image)
                 notification.getManager().notify((i + 1), notificationBuilder.build())
             }
         }
     }

     private fun getKeyTerms() {
         val list = ArrayList<String>()
         val ref = database.collection("users").document(mAuth.uid.toString())
         ref.get().addOnSuccessListener {snapshot ->
             if (snapshot != null) {
                 list.clear()
                 if (snapshot.data != null) {
                     val keyTerms = snapshot.data?.get("key_terms") as ArrayList<*>
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

     private fun getDurationTillNextNotification() {
         val ref = database.collection("users").document(mAuth.uid.toString())
         ref.get()
             .addOnSuccessListener { snapshot ->
                 if (snapshot != null) {
                     val duration = snapshot.get("duration") as Long
                     Log.e("gotNotification", duration.toString())
                     when (duration) {
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

     private fun scheduleNextNotification(time: Long) {
         Log.e("gotNotification", time.toString())
         val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
         val newIntent = Intent(context, NotificationReceiver::class.java)
         val pendingIntent = PendingIntent.getBroadcast(context, 1, newIntent, 0)
         val alarmTime = System.currentTimeMillis() + time
         alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
     }
}