package com.example.news_aggregator.utils

import android.content.Context
import com.example.news_aggregator.R
import com.example.news_aggregator.models.ArticleData
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.time.LocalDateTime

/**
 * Static class for getting articles from news api.
 */
class NewsAPI {

    companion object {
        /**
         * Calls the news api and gets a json of the articles. Then calls getListOfArticles to
         * to turn the json into an ArrayList of ArticleData.
         * @param endPoint String endpoint.
         * @param parameter String type of query.
         * @param query String what the query is.
         * @param sortBy String What to sort by.
         * @param forNotification Boolean Is the request for a notification.
         * @param context Context
         * @param onSuccess Function1<[@kotlin.ParameterName] ArrayList<ArticleData>, Unit>
         */
        fun getArticles(
            endPoint: String,
            parameter: String,
            query: String,
            sortBy: String,
            forNotification: Boolean,
            context: Context,
            onSuccess: (list: ArrayList<ArticleData>) -> Unit
        ) {
            val newsAPIKey = context.getString(R.string.news_api_key)
            val client = OkHttpClient()
            var list = ArrayList<ArticleData>()
            var jsonArray: JSONArray
            var date = LocalDateTime.now()
            //If the request is for a notification get articles from the past hour, otherwise
            //get articles from the past day.
            if (forNotification) {
                date.minusMinutes(60)
            } else {
                date = date.minusDays(1)
            }
            val url = context.getString(R.string.news_api_url) + endPoint + "?" +
                    parameter + "=" + query + "&" +
                    context.getString(R.string.news_api_url_from) + "=" + date + "&" +
                    context.getString(R.string.news_api_url_exclude_reuters) + "&" +
                    context.getString(R.string.news_api_url_sort) + "=" + sortBy + "&" + newsAPIKey
            val request = Request.Builder()
                .url(url)
                .build()
            client.newCall(request).enqueue(object : Callback {
                /**
                 * Failed to get articles from the news api.
                 * @param call Call
                 * @param e IOException
                 */
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                /**
                 * Got the articles from the news api. Call getListOfArticles to turn the json
                 * into an ArrayList of ArticleData. Pass the list into the callback funciton
                 * onSuccess.
                 * @param call Call
                 * @param response Response
                 */
                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        val json = JSONObject(response.body?.string()!!)
                        if (json.getString(context.getString(R.string.news_api_status)) != context.getString(
                                R.string.news_api_status_error
                            )
                        ) {
                            if (json.get(context.getString(R.string.news_api_total_results))
                                    .toString().toInt() != 0
                            ) {
                                jsonArray =
                                    json.getJSONArray(context.getString(R.string.news_api_articles))
                                list = getListOfArticles(jsonArray, context)
                            }
                            onSuccess(list)
                        }
                    }
                    response.close()
                }
            })
        }

        /**
         * Turn a json of articles into an ArrayList of ArticleData.
         * @param jsonArray JSONArray jsonArray from news api.
         * @param context Context
         * @return ArrayList<ArticleData>
         */
        fun getListOfArticles(jsonArray: JSONArray, context: Context): ArrayList<ArticleData> {
            val list = ArrayList<ArticleData>()
            val jsonArrayLength = jsonArray.length()
            var count = 20
            if (jsonArrayLength < 20) {
                count = jsonArrayLength
            }
            //Only get 20 articles.
            for (i in 0 until count) {
                val tempJson = jsonArray.getJSONObject(i)
                var author = tempJson.getString(context.getString(R.string.news_api_author))
                var publisher = tempJson.getJSONObject(context.getString(R.string.news_api_source))
                    .getString(context.getString(R.string.news_api_name))
                var publishedAt =
                    context.getString(R.string.news_api_date_text) + tempJson.getString(
                        context.getString(R.string.news_api_published_at)
                    )
                //If these values are blank, put "unknown" or "no description available".
                var description =
                    tempJson.getString(context.getString(R.string.news_api_description))
                if (author == "null" || author == "") {
                    author = context.getString(R.string.news_api_unknown)
                }
                if (publisher == "null" || publisher == "") {
                    publisher = context.getString(R.string.news_api_unknown)
                }
                if (description == "null" || description == "") {
                    description = context.getString(R.string.news_api_no_desc)
                }
                publishedAt = publishedAt.split("T")[0]
                list.add(
                    ArticleData(
                        tempJson.getString(context.getString(R.string.news_api_title)),
                        tempJson.getString(context.getString(R.string.news_api_url_image)),
                        context.getString(R.string.news_api_author_text) + author,
                        description,
                        context.getString(R.string.news_api_publisher_text) + publisher,
                        publishedAt,
                        tempJson.getString(context.getString(R.string.news_api_json_url))
                    )
                )
            }
            return list
        }
    }
}