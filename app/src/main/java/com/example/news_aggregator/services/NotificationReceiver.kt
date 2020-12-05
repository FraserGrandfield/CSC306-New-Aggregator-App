 package com.example.news_aggregator.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.example.news_aggregator.models.NewsAPI
import com.example.news_aggregator.models.NotificationHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

 class NotificationReceiver : BroadcastReceiver() {
    private lateinit var context: Context

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            this.context = context
        }
//        var time = intent?.extras?.getString("time")!!.toLong()
        getKeyTerms()
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)
        val alarmTime = System.currentTimeMillis() + 10000
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
    }

     private fun getLatestArticle(list: ArrayList<String>) {
         for (i in list.indices)
         NewsAPI.getArticles("top-headlines", "q", list[i], "publishedAt", true) { it1 ->
             val data = it1[0]
             val notification = NotificationHelper(context)
             notification.createChannel()
             val notificationBuilder = notification.getChannelNotification(data.title, data.summary)
             notification.getManager().notify((i + 1), notificationBuilder.build())
         }
     }

     private fun getKeyTerms() {
         val mAuth = FirebaseAuth.getInstance()
         val database = FirebaseFirestore.getInstance()
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

}