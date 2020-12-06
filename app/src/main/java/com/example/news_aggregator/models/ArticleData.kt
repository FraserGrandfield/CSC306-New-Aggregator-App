package com.example.news_aggregator.models

data class ArticleData(
    var title: String,
    var image: String,
    var author: String,
    var summary: String,
    var publisher: String,
    var datePublished: String,
    var articleURL: String,
)