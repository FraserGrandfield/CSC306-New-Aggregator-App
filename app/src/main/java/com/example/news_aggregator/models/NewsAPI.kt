package com.example.news_aggregator.models

import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CountDownLatch

class NewsAPI {

    companion object {
        //Spare key = bcba5b1f25f1446e9896fa7d58d81d2d
        const val NEWSAPI_KEY = "apiKey=68bef160bad148b98b324bfd65b522af"
        fun getArticles(endPoint: String, parameter: String, query: String, view: View) : ArrayList<DummyData> {
            val client = OkHttpClient()
            var list = ArrayList<DummyData>()
            var jsonArray: JSONArray
            val request = Request.Builder()
                .url("https://newsapi.org/v2/$endPoint?$parameter=$query&$NEWSAPI_KEY")
                .build()
            val countDownLatch = CountDownLatch(1)
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")
                        val responseData = response.body?.string()
                        //TODO check if 0 were returned
                        val json = JSONObject(responseData)
                        Log.e("Error", json.get("totalResults").toString())
                        if (json.get("totalResults").toString().toInt() == 0) {
                            Snackbar.make(view, "Error no articles match parameters", Snackbar.LENGTH_LONG).show()
                        } else {
                            jsonArray = json.getJSONArray("articles")
                            list = getListOfArticles(jsonArray)
                        }
                        countDownLatch.countDown()
                    }
                    response.close()
                }
            })
            countDownLatch.await()
            return list
        }

        fun getListOfArticles(jsonArray: JSONArray) : ArrayList<DummyData> {
            val list = ArrayList<DummyData>()
            for (i in 0 until 20) {
                val tempJson = jsonArray.getJSONObject(i)
                var author = "Author: " + tempJson.getString("author")
                var publisher = "Publisher: " + tempJson.getJSONObject("source").getString("name")
                var publishedAt = "Date: " + tempJson.getString("publishedAt")
                if (author == "null" || author == "") {
                    author = "Unknown"
                }
                if (publisher == "null" || publisher == "") {
                    publisher = "Unknown"
                }
                publishedAt = publishedAt.split("T")[0]
                list.add(
                    DummyData(
                        tempJson.getString("title"),
                        tempJson.getString("urlToImage"),
                        author,
                        tempJson.getString("description"),
                        publisher,
                        publishedAt,
                        tempJson.getString("url")
                    )
                )
            }
            return list
        }

    }
}