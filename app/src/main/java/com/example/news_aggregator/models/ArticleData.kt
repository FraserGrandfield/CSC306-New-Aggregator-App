package com.example.news_aggregator.models

/**
 * Data class for article data.
 * @property title String
 * @property image String
 * @property author String
 * @property summary String
 * @property publisher String
 * @property datePublished String
 * @property articleURL String
 * @constructor
 */
data class ArticleData(
    var title: String,
    var image: String,
    var author: String,
    var summary: String,
    var publisher: String,
    var datePublished: String,
    var articleURL: String,
)