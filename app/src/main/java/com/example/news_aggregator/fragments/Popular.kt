package com.example.news_aggregator.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news_aggregator.R
import com.example.news_aggregator.adapters.ArticleRecyclerAdapter
import com.example.news_aggregator.interfaces.TopSpacingItemDecoration
import com.example.news_aggregator.models.DummyData
import com.example.news_aggregator.models.NewsAPI
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_for_you.*

class Popular : Fragment() {
    //TODO add onResueme and refresh the articles
    private lateinit var articleAdapter: ArticleRecyclerAdapter
    private lateinit var database: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseFirestore.getInstance()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_popular, container, false)
    }

    companion object {
        fun newInstance() = Popular().apply { arguments = Bundle().apply {
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            articleAdapter = ArticleRecyclerAdapter()
            adapter = articleAdapter
        }
        addDataSet()
    }

    private fun addDataSet() {
        var ref = database.collection("liked_articles").orderBy("likes", Query.Direction.DESCENDING).limit(20)
        var list = ArrayList<DummyData>()
        ref.get().addOnSuccessListener { documents ->
            if (documents.isEmpty) {
                view?.let { NewsAPI.getArticles("top-headlines", "country", "gb", "publishedAt", false) { it1 ->
                    articleAdapter.submitList(it1)
                    activity?.runOnUiThread {
                        articleAdapter.notifyDataSetChanged()
                    } } }
            } else {
                for (document in documents) {
                    Log.e("Error", document.get("article_url").toString())
                    list.add(
                        DummyData(
                            document.get("title").toString(),
                            document.get("image").toString(),
                            document.get("author").toString(),
                            document.get("summary").toString(),
                            document.get("publisher").toString(),
                            document.get("date_published").toString(),
                            document.get("article_url").toString(),
                        ))
                }
            }
            articleAdapter.submitList(list)
            activity?.runOnUiThread {
                articleAdapter.notifyDataSetChanged()
            }
        }.addOnFailureListener { } //TODO add error

    }
}