package com.example.news_aggregator.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.viewpager.widget.ViewPager
import com.example.news_aggregator.R
import com.example.news_aggregator.adapters.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        val toolbar = top_app_bar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false);
        val drawerLayout = drawer
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav_draw, R.string.close_nav_draw)
        drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()
        val navigationView = nav_view
        navigationView.setNavigationItemSelectedListener { menuItem ->
            val id = menuItem.itemId
            if (id == R.id.log_out) {
//                val intent = Intent(this, LogInActivity::class.java)
//                startActivity(intent)
            } else if (id == R.id.create_account) {
                val intent = Intent(this, CreateAccountActivity::class.java)
                startActivity(intent)
            } else if (id == R.id.log_in) {
                val intent = Intent(this, LogInActivity::class.java)
                startActivity(intent)
            }
            drawer.closeDrawer(GravityCompat.START)
            true
        }

    }
}