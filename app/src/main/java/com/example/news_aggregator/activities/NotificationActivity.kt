package com.example.news_aggregator.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.news_aggregator.R
import com.example.news_aggregator.services.NotificationReceiver
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_notifications.*
import kotlinx.android.synthetic.main.content_main.top_app_bar

class NotificationActivity : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        val toolbar = top_app_bar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mAuth = FirebaseAuth.getInstance()

        val spinner = spinner_notifications
        ArrayAdapter.createFromResource(this,
            R.array.notification_array,
            android.R.layout.simple_spinner_item
        ).also{ adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        val button = notification_button
        button.setOnClickListener {
            val input = spinner_notifications.selectedItemPosition
            var alarmTime = System.currentTimeMillis()
            if (input == 1) {
                cancelAlarmManager()
                startAlarmManager(alarmTime + 10000)
                Log.e("notification", "1")
            } else if (input == 2) {
                cancelAlarmManager()
                startAlarmManager(alarmTime + 10000 * 2)
                Log.e("notification", "2")
            }
            else if (input == 3) {
                cancelAlarmManager()
                startAlarmManager(alarmTime + 10000 * 5)
                Log.e("notification", "3")
            } else if (input == 0) {
                cancelAlarmManager() }

        }
    }

    private fun startAlarmManager(time: Long) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationReceiver::class.java)
        intent.putExtra("time", time)
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    private fun cancelAlarmManager() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0)
        alarmManager.cancel(pendingIntent)
    }

}