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
        //TODO add time to firebase and pull here to get time
        getDurationTillNextNotification()
        getKeyTerms()
    }

     private fun getLatestArticle(list: ArrayList<String>) {
         for (i in list.indices)
             //TODO Improve look of notification, and add funciton where if click the notificaiton it will open the article.
         NewsAPI.getArticles("top-headlines", "q", list[i], "publishedAt", true) { it1 ->
             val data = it1[0]
             val notification = NotificationHelper(context)
             notification.createChannel()
             val notificationBuilder = notification.getChannelNotification(data.title, data.summary)
             notification.getManager().notify((i + 1), notificationBuilder.build())
         }
     }

     private fun getKeyTerms() {
        //TODO change to get instead of snapshot listener
         val list = ArrayList<String>()
         val ref = database.collection("users").document(mAuth.uid.toString())
         ref.addSnapshotListener {snapshot, e ->
             if (e != null) {
                 Log.w("Error", "Listen failed.", e)
             }
             if (snapshot != null && snapshot.exists()) {
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
                             val time: Long = 10000
                             scheduleNextNotification(time)
                         }
                         12L -> {
                             val time: Long = 10000 * 2
                             scheduleNextNotification(time)
                         }
                         24L -> {
                             val time: Long = 10000 * 5
                             scheduleNextNotification(time)
                         }
                     }
                 }

             }
             .addOnFailureListener { //TODO add logcat error
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