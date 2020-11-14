package com.example.news_aggregator.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import androidx.recyclerview.widget.RecyclerView
import com.example.news_aggregator.R
import com.example.news_aggregator.activities.ArticleActivity
import com.example.news_aggregator.models.DummyData
import kotlinx.android.synthetic.main.article_list_item.view.*

class ArticleRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<DummyData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.article_list_item, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ArticleViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(articleList: List<DummyData>) {
        items = articleList
    }

    class ArticleViewHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val articleImage: ImageView = itemView.article_image
        private val articleTitle: TextView = itemView.article_title
        private val articleAuthor = itemView.article_author
        private val articlePublisher = itemView.article_publisher
        private val articleButton = itemView.article_button
        private val articlePublishedAt = itemView.article_published_at

        fun bind(dummyData: DummyData) {
            articleTitle.text = dummyData.title
            articleAuthor.text = dummyData.author
            articlePublisher.text = dummyData.publisher
            articlePublishedAt.text = dummyData.datePublished
            val requestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background).error(
                R.drawable.ic_launcher_background
            )
            val context : Context = itemView.context
            Glide.with(itemView.context).applyDefaultRequestOptions(requestOptions).load(dummyData.image).into(articleImage)
            articleButton.setOnClickListener {
                val intent = Intent(context, ArticleActivity::class.java)
                intent.putExtra("title", dummyData.title)
                intent.putExtra("summary", dummyData.summary)
                intent.putExtra("author", dummyData.author)
                intent.putExtra("publisher", dummyData.publisher)
                intent.putExtra("image", dummyData.image)
                context.startActivity(intent)
            }
        }

    }
}