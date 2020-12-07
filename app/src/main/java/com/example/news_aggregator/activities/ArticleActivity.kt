package com.example.news_aggregator.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.news_aggregator.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_article.*
import kotlinx.android.synthetic.main.content_main.top_app_bar

/**
 * Activity for one article
 * @property mAuth FirebaseAuth
 * @property url String
 */
class ArticleActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var url: String

    /**
     * Gets all the intents extras and displays the information.
     * @param savedInstanceState Bundle?
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)
        mAuth = FirebaseAuth.getInstance()
        val toolbar = top_app_bar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        textViewTitle.text = intent.getStringExtra(getString(R.string.article_data_title))
        textViewSummary.text = intent.getStringExtra(getString(R.string.article_data_summary))
        textViePublisher.text = intent.getStringExtra(getString(R.string.article_data_publisher))
        textViewAuthor.text = intent.getStringExtra(getString(R.string.article_data_author))
        url = intent.getStringExtra(getString(R.string.article_data_article_url))!!
        val requestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background).error(
            R.drawable.ic_launcher_background
        )
        Glide.with(this).applyDefaultRequestOptions(requestOptions)
            .load(intent.getStringExtra("image")).centerCrop().into(imageView)
    }

    /**
     * Onclick function to open the web view activity for the article.
     * @param view View
     */
    fun webViewButtonOnClick(view: View) {
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra(getString(R.string.article_data_article_url), url)
        startActivity(intent)
    }
}