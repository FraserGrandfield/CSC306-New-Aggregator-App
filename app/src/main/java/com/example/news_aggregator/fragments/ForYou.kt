package com.example.news_aggregator.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news_aggregator.adapters.ArticleRecyclerAdapter
import com.example.news_aggregator.models.DataSource
import com.example.news_aggregator.R
import com.example.news_aggregator.interfaces.TopSpacingItemDecoration
import com.example.news_aggregator.models.NewsAPI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_for_you.*

class ForYou : Fragment() {

    private lateinit var articleAdapter: ArticleRecyclerAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var ref: DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_for_you, container, false)
    }

    companion object {
        fun newInstance() = ForYou().apply { arguments = Bundle().apply {
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
        if (mAuth.currentUser == null) {
            val list = NewsAPI.getArticles("top-headlines", "country", "gb")
            articleAdapter.submitList(list)
            articleAdapter.notifyDataSetChanged()
        } else {
            var parameters = ""
            ref = database.collection("users").document(mAuth.uid.toString())
            ref.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val keyTerms = document.data?.get("key_terms") as ArrayList<*>
                    for (term in keyTerms) {
                        parameters += "$term OR "
                    }
                    parameters = parameters.dropLast(4)
                    if (parameters == "") {
                        val list = NewsAPI.getArticles("top-headlines", "country", "gb")
                        articleAdapter.submitList(list)
                        articleAdapter.notifyDataSetChanged()
                    } else {
                        val list = NewsAPI.getArticles("everything", "q", parameters)
                        articleAdapter.submitList(list)
                        articleAdapter.notifyDataSetChanged()
                    }
                } else {
                    Log.d("Error", "Current data: null")
                }
            }.addOnFailureListener { } //TODO add error
        }
    }
}