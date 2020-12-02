package com.example.news_aggregator.activities

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.news_aggregator.R
import com.example.news_aggregator.services.NotificationService
import com.google.firebase.auth.FirebaseAuth
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

        val component = ComponentName(baseContext, NotificationService::class.java)
        val jobinfo = JobInfo.Builder(1, component).setPersisted(true).build()
        val jobSchedular = this.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobSchedular.schedule(jobinfo)
    }

}