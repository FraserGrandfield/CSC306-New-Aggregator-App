package com.example.news_aggregator.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news_aggregator.R
import com.example.news_aggregator.adapters.ArticleRecyclerAdapter
import com.example.news_aggregator.interfaces.TopSpacingItemDecoration
import com.example.news_aggregator.models.DataSource
import com.example.news_aggregator.models.NewsAPI
import kotlinx.android.synthetic.main.fragment_for_you.*

class Local : Fragment() {

    private lateinit var articleAdapter: ArticleRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_local, container, false)
    }

    companion object {
        fun newInstance() = Local().apply { arguments = Bundle().apply {
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
//        val data = DataSource.createDataSet()
        val list = this.view?.let { NewsAPI.getArticles("top-headlines", "country", "us", it) }
        if (list != null) {
            articleAdapter.submitList(list)
        }
    }

}