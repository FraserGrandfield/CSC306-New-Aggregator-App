package com.example.news_aggregator.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.news_aggregator.R
import com.example.news_aggregator.activities.ArticleActivity
import com.example.news_aggregator.models.ArticleData
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.article_list_item.view.*

class ArticleRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<ArticleData> = ArrayList()

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

    fun submitList(articleList: List<ArticleData>) {
        items = articleList
    }

    class ArticleViewHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val mAuth = FirebaseAuth.getInstance()

        private val articleImage: ImageView = itemView.article_image
        private val articleTitle: TextView = itemView.article_title
        private val articleAuthor = itemView.article_author
        private val articlePublisher = itemView.article_publisher
        private val articleButton = itemView.article_button
        private val articlePublishedAt = itemView.article_published_at
        private val likeButton = itemView.appCompatCheckBox

        fun bind(articleData: ArticleData) {
            articleTitle.text = articleData.title
            articleAuthor.text = articleData.author
            articlePublisher.text = articleData.publisher
            articlePublishedAt.text = articleData.datePublished
            val requestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background).error(
                R.drawable.ic_launcher_background
            )
            val context : Context = itemView.context
            Glide.with(itemView.context).applyDefaultRequestOptions(requestOptions).load(articleData.image).into(articleImage)
            articleButton.setOnClickListener {
                val intent = Intent(context, ArticleActivity::class.java)
                intent.putExtra("title", articleData.title)
                intent.putExtra("summary", articleData.summary)
                intent.putExtra("author", articleData.author)
                intent.putExtra("publisher", articleData.publisher)
                intent.putExtra("image", articleData.image)
                intent.putExtra("url", articleData.articleURL)
                context.startActivity(intent)
            }

            val database = FirebaseFirestore.getInstance()
            val articleURL = articleData.articleURL.replace("/", "")
            val ref = database.collection("liked_articles").document(articleURL)
            ref.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    itemView.article_likes.text = document.get("likes").toString()
                } else {
                    itemView.article_likes.text = "0"
                }
            }.addOnFailureListener {
                Log.e("ArticleRecyclerAdapter", "Ref listener failed: $it")
            }

            ref.collection("liked_users").document(mAuth.uid.toString()).get().addOnSuccessListener { document ->
                likeButton.isChecked = document.exists()
                addLikeButtonListener(articleData)
            }.addOnFailureListener {
                Log.e("ArticleRecyclerAdapter", "Ref listener failed: $it")
            }
        }

        private fun addLikeButtonListener(articleData: ArticleData) {

            likeButton.setOnClickListener {
                if (mAuth.currentUser != null) {
                    val database = FirebaseFirestore.getInstance()
                    val articleURL = articleData.articleURL.replace("/", "")
                    val ref = database.collection("liked_articles").document(articleURL)
                    if (likeButton.isChecked) {
                        val likes = itemView.article_likes.text.toString().toInt()
                        itemView.article_likes.text = (likes + 1).toString()
                        ref.get().addOnSuccessListener { document ->
                            if (document.exists()) {
                                ref.update("likes", FieldValue.increment(1))
                                val map = hashMapOf(
                                    "uid" to mAuth.uid.toString()
                                )
                                ref.collection("liked_users").document(mAuth.uid.toString()).set(map)
                            } else {
                                val map = hashMapOf (
                                    "likes" to 1,
                                    "title" to articleData.title,
                                    "summary" to articleData.summary,
                                    "publisher" to articleData.publisher,
                                    "author" to articleData.author,
                                    "image" to articleData.image,
                                    "date_published" to articleData.datePublished,
                                    "article_url" to articleData.articleURL,
                                )
                                ref.set(map)
                                ref.collection("liked_users").document(mAuth.uid.toString()).set(map)
                            }
                        }.addOnFailureListener {exception ->
                            Log.e("ArticleRecyclerAdapter", "Ref listener failed: $exception")
                        }
                    } else {
                        val likes = itemView.article_likes.text.toString().toInt()
                        itemView.article_likes.text = (likes - 1).toString()
                        ref.update("likes", FieldValue.increment(-1))
                        ref.collection("liked_users").document(mAuth.uid.toString()).delete()
                    }
                } else {
                    likeButton.isChecked = false
                    Snackbar.make(it, "You need to sign in", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}