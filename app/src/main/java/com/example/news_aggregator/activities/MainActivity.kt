package com.example.news_aggregator.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.news_aggregator.R
import com.example.news_aggregator.adapters.SectionsPagerAdapter
import com.example.news_aggregator.receivers.NotificationReceiver
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        view_pager.adapter = SectionsPagerAdapter(this)
        TabLayoutMediator(tabs, view_pager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.tab_text_1)
                }
                1 -> {
                    tab.text = getString(R.string.tab_text_2)
                }
                2 -> {
                    tab.text = getString(R.string.tab_text_3)
                }
            }
        }.attach()

        val toolbar = top_app_bar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val drawerLayout = drawer
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav_draw,
            R.string.close_nav_draw
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()
        val navigationView = nav_view
        changeNavItems(navigationView.menu)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.log_out -> {
                    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val intent = Intent(this, NotificationReceiver::class.java)
                    val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0)
                    alarmManager.cancel(pendingIntent)
                    mAuth.signOut()
                    this.recreate()
                }
                R.id.create_account -> {
                    val intent = Intent(this, CreateAccountActivity::class.java)
                    startActivity(intent)
                }
                R.id.log_in -> {
                    val intent = Intent(this, LogInActivity::class.java)
                    startActivity(intent)
                }
                R.id.key_terms -> {
                    val intent = Intent(this, KeyTermsActivity::class.java)
                    startActivity(intent)
                }
                R.id.notifications -> {
                    val intent = Intent(this, NotificationActivity::class.java)
                    startActivity(intent)
                }
            }
            drawer.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun changeNavItems(menu : Menu) {
        if (mAuth.currentUser == null) {
            menu.getItem(0).isVisible = false
            menu.getItem(1).isVisible = false
            menu.getItem(2).isVisible = false
            menu.getItem(3).isVisible = true
            menu.getItem(4).isVisible = true
        } else {
            menu.getItem(0).isVisible = true
            menu.getItem(1).isVisible = true
            menu.getItem(2).isVisible = true
            menu.getItem(3).isVisible = false
            menu.getItem(4).isVisible = false
        }
    }
}