package com.example.news_aggregator.models

import com.google.firebase.database.FirebaseDatabase

class DataBaseModel {
    companion object {
        fun addKeyTerm(userID : String, keyTerm : String) {
            val database : FirebaseDatabase = FirebaseDatabase.getInstance()
            val ref = database.getReference(userID)
            ref.child(keyTerm).setValue(keyTerm)
        }
    }
}