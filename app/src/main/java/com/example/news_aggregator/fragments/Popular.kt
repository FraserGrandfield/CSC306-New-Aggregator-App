package com.example.news_aggregator.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news_aggregator.R
import com.example.news_aggregator.adapters.ArticleRecyclerAdapter
import com.example.news_aggregator.interfaces.TopSpacingItemDecoration
import com.example.news_aggregator.models.ArticleData
import com.example.news_aggregator.utils.NewsAPI
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_for_you.*

/**
 * Popular fragment for displaying popular articles to the user.
 * @property articleAdapter ArticleRecyclerAdapter
 * @property database FirebaseFirestore
 */
class Popular : Fragment() {
    private lateinit var articleAdapter: ArticleRecyclerAdapter
    private lateinit var database: FirebaseFirestore

    /**
     * On create function.
     * @param savedInstanceState Bundle?
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseFirestore.getInstance()
    }

    /**
     * Inflate the popular fragment.
     * @param inflater LayoutInflater
     * @param container ViewGroup?
     * @param savedInstanceState Bundle?
     * @return View?
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_popular, container, false)
    }
    companion object {
        fun newInstance() = Popular().apply {
            arguments = Bundle().apply {
            }
        }
    }

    /**
     * Attach the adapter to the recycle view.
     * @param view View
     * @param savedInstanceState Bundle?
     */
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

    /**
     * call addDataSet when the fragment is shown.
     */
    override fun onResume() {
        super.onResume()
        addDataSet()
    }

    /**
     * Gets the top 20 most liked articles from FireStore and displays submits them to the adapter.
     */
    private fun addDataSet() {
        val ref = database.collection(getString(R.string.firestore_liked_articles))
            .orderBy(getString(R.string.firestore_likes), Query.Direction.DESCENDING).limit(20)
        val list = ArrayList<ArticleData>()
        ref.get().addOnSuccessListener { documents ->
            //If there are no liked articles then display top headlines.
            if (documents.isEmpty) {
                view?.let {
                    context?.let { it1 ->
                        NewsAPI.getArticles(
                            getString(R.string.news_api_top_headlines),
                            getString(R.string.news_api_country),
                            getString(R.string.news_api_gb),
                            getString(R.string.news_api_published_at),
                            false,
                            it1
                        ) { list ->
                            articleAdapter.submitList(list)
                            activity?.runOnUiThread {
                                articleAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            } else {
                for (document in documents) {
                    list.add(
                        ArticleData(
                            document.get(getString(R.string.firestore_title)).toString(),
                            document.get(getString(R.string.firestore_image)).toString(),
                            document.get(getString(R.string.firestore_author)).toString(),
                            document.get(getString(R.string.firestore_summary)).toString(),
                            document.get(getString(R.string.firestore_publisher)).toString(),
                            document.get(getString(R.string.firestore_date_published)).toString(),
                            document.get(getString(R.string.firestore_article_url)).toString(),
                        )
                    )
                }
            }
            articleAdapter.submitList(list)
            activity?.runOnUiThread {
                articleAdapter.notifyDataSetChanged()
            }
        }.addOnFailureListener {
            view?.let {
                Snackbar.make(
                    it,
                    getString(R.string.snackbar_cannot_get_popular),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

    }
}