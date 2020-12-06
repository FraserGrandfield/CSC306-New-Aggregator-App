package com.example.news_aggregator.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.get
import com.example.news_aggregator.R
import com.example.news_aggregator.services.NotificationReceiver
import com.google.android.material.textfield.TextInputLayout
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
        mAuth = FirebaseAuth.getInstance()
        (notification_menu.editText as? AutoCompleteTextView)?.inputType = EditorInfo.TYPE_NULL

        val items = listOf(getString(R.string.never), getString(R.string._6_hours), getString(R.string._12_hours), getString(R.string._24_hours))
        val adapter = ArrayAdapter(this.applicationContext, R.layout.navigation_list_item, items)
        (notification_menu.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        val button = notification_button
        button.setOnClickListener {
            val input = filled_exposed_dropdown.text.toString()
            val alarmTime = System.currentTimeMillis()
            when (input) {
                getString(R.string._6_hours) -> {
                    cancelAlarmManager()
                    startAlarmManager(alarmTime + 10000)
                    Log.e("notification", "1")
                }
                getString(R.string._12_hours) -> {
                    cancelAlarmManager()
                    startAlarmManager(alarmTime + 10000 * 2)
                    Log.e("notification", "2")
                }
                getString(R.string._24_hours) -> {
                    cancelAlarmManager()
                    startAlarmManager(alarmTime + 10000 * 5)
                    Log.e("notification", "3")
                }
                getString(R.string.never) -> {
                    cancelAlarmManager() }
            }
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