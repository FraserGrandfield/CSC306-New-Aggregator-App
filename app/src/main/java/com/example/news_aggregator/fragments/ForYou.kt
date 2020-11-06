package com.example.news_aggregator.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news_aggregator.adapters.ArticleRecyclerAdapter
import com.example.news_aggregator.models.DataSource
import com.example.news_aggregator.R
import com.example.news_aggregator.interfaces.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_for_you.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ForYou : Fragment() {

    private lateinit var articleAdapter: ArticleRecyclerAdapter
    private lateinit var thisContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
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
        val data = DataSource.createDataSet()
        //val list = data[0]
        //articleAdapter.submitList(list)
    }

}