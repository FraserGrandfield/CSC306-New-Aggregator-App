package com.example.news_aggregator.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.news_aggregator.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_article.*
import kotlinx.android.synthetic.main.article_list_item.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.top_app_bar

class ArticleActivity : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)
        mAuth = FirebaseAuth.getInstance()
        val toolbar = top_app_bar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        textViewTitle.text = intent.getStringExtra("title")
        textViewSummary.text = intent.getStringExtra("summary")
        textViePublisher.text = intent.getStringExtra("publisher")
        textViewAuthor.text = intent.getStringExtra("author")

        val requestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background).error(
            R.drawable.ic_launcher_background
        )
        Glide.with(this).applyDefaultRequestOptions(requestOptions).load(intent.getStringExtra("image")).into(imageView)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
    }

//    fun buttonOnClick(view: View) {
//        val intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
//    }
}