package com.example.news_aggregator.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.example.news_aggregator.R
import com.example.news_aggregator.services.NotificationReceiver
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_key_terms.*
import kotlinx.android.synthetic.main.activity_notifications.*
import kotlinx.android.synthetic.main.content_main.top_app_bar

class NotificationActivity : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth
    private lateinit var database: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        val toolbar = top_app_bar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        (notification_menu.editText as? AutoCompleteTextView)?.inputType = EditorInfo.TYPE_NULL
        //TODO get saved time from database and display in spinner
        val items = listOf(getString(R.string.never), getString(R.string._6_hours), getString(R.string._12_hours), getString(R.string._24_hours))
        val adapter = ArrayAdapter(this.applicationContext, R.layout.navigation_list_item, items)
        (notification_menu.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        val button = notification_button
        button.setOnClickListener {
            val input = filled_exposed_dropdown.text.toString()
            when (input) {
                getString(R.string._6_hours) -> {
                    cancelAlarmManager()
                    addSavedTimeToAccount(10000, 6)

                }
                getString(R.string._12_hours) -> {
                    cancelAlarmManager()
                    addSavedTimeToAccount(10000 * 2, 12)

                }
                getString(R.string._24_hours) -> {
                    cancelAlarmManager()
                    addSavedTimeToAccount(10000 * 5, 24)

                }
                getString(R.string.never) -> {
                    cancelAlarmManager()
                }
            }
        }
    }

    private fun startAlarmManager(time: Long) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationReceiver::class.java)
        val alarmTime = System.currentTimeMillis()
        //TODO issue where changing time gives notification straight away
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime + time, pendingIntent)
    }

    private fun cancelAlarmManager() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0)
        alarmManager.cancel(pendingIntent)
    }

    private fun addSavedTimeToAccount(time: Long, duration: Int) {
        val ref = database.collection("users").document(mAuth.uid.toString())
        ref.update("duration", duration)
            .addOnSuccessListener {
                startAlarmManager(time)
            }.addOnFailureListener {
                //TODO add snackbar for error adding time to database
            }
    }
}