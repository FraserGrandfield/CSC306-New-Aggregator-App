package com.example.news_aggregator.models

import android.content.Context
import android.util.Log
import com.example.news_aggregator.R
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.time.LocalDateTime

class NewsAPI{

    companion object {
        //Spare key = 9e0bdb83896e47da8af5e964329eaaec  68bef160bad148b98b324bfd65b522af    bcba5b1f25f1446e9896fa7d58d81d2d   a2afcd06f1a54787b44592b4d6f1c116  14751837a2364903a7572d7689bf0c9e
        fun getArticles(endPoint: String, parameter: String, query: String,sortBy: String, forNotification: Boolean, context: Context, onSuccess: (list: ArrayList<ArticleData>) -> Unit) {
            val newsAPIKey = context.getString(R.string.news_api_key)
            val client = OkHttpClient()
            var list = ArrayList<ArticleData>()
            var jsonArray: JSONArray
            var date = LocalDateTime.now()
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
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        val json = JSONObject(response.body?.string()!!)
                        if (json.getString("status") != "error") {
                            if (json.get("totalResults").toString().toInt() != 0) {
                                jsonArray = json.getJSONArray("articles")
                                list = getListOfArticles(jsonArray)
                            }
                            onSuccess(list)
                        } else {
                            //TODO snackbar error
                        }
                    }
                    response.close()
                }
            })
        }

        fun getListOfArticles(jsonArray: JSONArray) : ArrayList<ArticleData> {
            val list = ArrayList<ArticleData>()
            val jsonArrayLength = jsonArray.length()
            var count = 20
            if (jsonArrayLength < 20) {
                count = jsonArrayLength
            }
            for (i in 0 until count) {
                val tempJson = jsonArray.getJSONObject(i)
                var author = tempJson.getString("author")
                var publisher = tempJson.getJSONObject("source").getString("name")
                var publishedAt = "Date: " + tempJson.getString("publishedAt")
                var description = tempJson.getString("description")
                if (author == "null" || author == "") {
                    author = "Unknown"
                }
                if (publisher == "null" || publisher == "") {
                    publisher = "Unknown"
                }
                if (description == "null" || description == "") {
                    description = "No description available"
                }
                publishedAt = publishedAt.split("T")[0]
                list.add(
                    ArticleData(
                        tempJson.getString("title"),
                        tempJson.getString("urlToImage"),
                        "Author: $author",
                        description,
                        "Publisher: $publisher",
                        publishedAt,
                        tempJson.getString("url")
                    )
                )
            }
            return list
        }

    }
}