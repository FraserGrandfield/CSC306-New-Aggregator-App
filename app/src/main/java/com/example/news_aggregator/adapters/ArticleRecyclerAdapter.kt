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

/**
 * Adapter for the article recycle views.
 * @property items List<ArticleData>
 */
class ArticleRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: List<ArticleData> = ArrayList()

    /**
     * Inflating the article list item.
     * @param parent ViewGroup
     * @param viewType Int
     * @return RecyclerView.ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.article_list_item, parent, false)
        return ArticleViewHolder(view)
    }

    /**
     * Binds each article.
     * @param holder ViewHolder
     * @param position Int
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ArticleViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    /**
     * Get the number of articles.
     * @return Int
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * Pass the list of articles to the adapter to be binded.
     * @param articleList List<ArticleData>
     */
    fun submitList(articleList: List<ArticleData>) {
        items = articleList
    }

    /**
     * Article view holder.
     * @property mAuth FirebaseAuth
     * @property articleImage ImageView
     * @property articleTitle TextView
     * @property articleAuthor (androidx.appcompat.widget.AppCompatTextView..androidx.appcompat.widget.AppCompatTextView?)
     * @property articlePublisher (androidx.appcompat.widget.AppCompatTextView..androidx.appcompat.widget.AppCompatTextView?)
     * @property articleButton (com.google.android.material.button.MaterialButton..com.google.android.material.button.MaterialButton?)
     * @property articlePublishedAt (androidx.appcompat.widget.AppCompatTextView..androidx.appcompat.widget.AppCompatTextView?)
     * @property likeButton (com.google.android.material.checkbox.MaterialCheckBox..com.google.android.material.checkbox.MaterialCheckBox?)
     * @constructor
     */
    class ArticleViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mAuth = FirebaseAuth.getInstance()
        private val articleImage: ImageView = itemView.article_image
        private val articleTitle: TextView = itemView.article_title
        private val articleAuthor = itemView.article_author
        private val articlePublisher = itemView.article_publisher
        private val articleButton = itemView.article_button
        private val articlePublishedAt = itemView.article_published_at
        private val likeButton = itemView.appCompatCheckBox

        /**
         * bind each article.
         * @param articleData ArticleData
         */
        fun bind(articleData: ArticleData) {
            articleTitle.text = articleData.title
            articleAuthor.text = articleData.author
            articlePublisher.text = articleData.publisher
            articlePublishedAt.text = articleData.datePublished
            val requestOptions =
                RequestOptions().placeholder(R.drawable.ic_launcher_background).error(
                    R.drawable.ic_launcher_background
                )
            val context: Context = itemView.context
            //Display the image.
            Glide.with(itemView.context).applyDefaultRequestOptions(requestOptions)
                .load(articleData.image).into(articleImage)
            //On click listener for when the user clicks to read more on an article.
            articleButton.setOnClickListener {
                val intent = Intent(context, ArticleActivity::class.java)
                intent.putExtra(context.getString(R.string.article_data_title), articleData.title)
                intent.putExtra(
                    context.getString(R.string.article_data_summary),
                    articleData.summary
                )
                intent.putExtra(context.getString(R.string.article_data_author), articleData.author)
                intent.putExtra(
                    context.getString(R.string.article_data_publisher),
                    articleData.publisher
                )
                intent.putExtra(context.getString(R.string.article_data_image), articleData.image)
                intent.putExtra(
                    context.getString(R.string.article_data_article_url),
                    articleData.articleURL
                )
                context.startActivity(intent)
            }
            //Displaying the amount of likes the article has.
            val database = FirebaseFirestore.getInstance()
            val articleURL = articleData.articleURL.replace("/", "")
            val ref = database.collection(context.getString(R.string.firestore_liked_articles))
                .document(articleURL)
            ref.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    itemView.article_likes.text =
                        document.get(context.getString(R.string.firestore_likes)).toString()
                } else {
                    itemView.article_likes.text = context.getString(R.string.zero)
                }
            }.addOnFailureListener {
                Log.e("ArticleRecyclerAdapter", "Ref listener failed: $it")
            }
            ref.collection(context.getString(R.string.firestore_liked_users))
                .document(mAuth.uid.toString()).get().addOnSuccessListener { document ->
                likeButton.isChecked = document.exists()
                addLikeButtonListener(articleData, context)
            }.addOnFailureListener {
                Log.e("ArticleRecyclerAdapter", "Ref listener failed: $it")
            }
        }

        /**
         * On click for liking and disliking the article.
         * @param articleData ArticleData
         * @param context Context
         */
        private fun addLikeButtonListener(articleData: ArticleData, context: Context) {
            likeButton.setOnClickListener {
                if (mAuth.currentUser != null) {
                    val database = FirebaseFirestore.getInstance()
                    val articleURL = articleData.articleURL.replace("/", "")
                    val ref =
                        database.collection(context.getString(R.string.firestore_liked_articles))
                            .document(articleURL)
                    if (likeButton.isChecked) {
                        val likes = itemView.article_likes.text.toString().toInt()
                        itemView.article_likes.text = (likes + 1).toString()
                        ref.get().addOnSuccessListener { document ->
                            if (document.exists()) {
                                ref.update(
                                    context.getString(R.string.firestore_likes),
                                    FieldValue.increment(1)
                                )
                                val map = hashMapOf(
                                    context.getString(R.string.firestore_uid) to mAuth.uid.toString()
                                )
                                ref.collection(context.getString(R.string.firestore_liked_users))
                                    .document(mAuth.uid.toString()).set(map)
                            } else {
                                val map = hashMapOf(
                                    context.getString(R.string.firestore_likes) to 1,
                                    context.getString(R.string.firestore_title) to articleData.title,
                                    context.getString(R.string.firestore_summary) to articleData.summary,
                                    context.getString(R.string.firestore_publisher) to articleData.publisher,
                                    context.getString(R.string.firestore_author) to articleData.author,
                                    context.getString(R.string.firestore_image) to articleData.image,
                                    context.getString(R.string.firestore_date_published) to articleData.datePublished,
                                    context.getString(R.string.firestore_article_url) to articleData.articleURL,
                                )
                                ref.set(map)
                                ref.collection(context.getString(R.string.firestore_liked_users))
                                    .document(mAuth.uid.toString()).set(map)
                            }
                        }.addOnFailureListener { exception ->
                            Log.e("ArticleRecyclerAdapter", "Ref listener failed: $exception")
                        }
                    } else {
                        val likes = itemView.article_likes.text.toString().toInt()
                        itemView.article_likes.text = (likes - 1).toString()
                        ref.update(
                            context.getString(R.string.firestore_likes),
                            FieldValue.increment(-1)
                        )
                        ref.collection(context.getString(R.string.firestore_liked_users))
                            .document(mAuth.uid.toString()).delete()
                    }
                } else {
                    likeButton.isChecked = false
                    Snackbar.make(
                        it,
                        context.getString(R.string.snackbar_not_signed_in),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}