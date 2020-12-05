package com.example.news_aggregator.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news_aggregator.R
import com.example.news_aggregator.adapters.KeyTermRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_key_terms.*
import kotlinx.android.synthetic.main.content_main.top_app_bar
import kotlinx.android.synthetic.main.fragment_for_you.recycler_view

class KeyTermsActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var KeyTermAdapter: KeyTermRecyclerAdapter
    private var list = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_key_terms)
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        val toolbar = top_app_bar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        recycler_view.apply {
            layoutManager = LinearLayoutManager(this.context)
            KeyTermAdapter = KeyTermRecyclerAdapter()
            adapter = KeyTermAdapter
        }

        val ref = database.collection("users").document(mAuth.uid.toString())
        ref.addSnapshotListener {snapshot, e ->
            if (e != null) {
                Log.w("Error", "Listen failed.", e)
            }
            if (snapshot != null && snapshot.exists()) {
                list.clear()
                if (snapshot.data != null) {
                    val keyTerms = snapshot.data?.get("key_terms") as ArrayList<*>
                    for (term in keyTerms) {
                        list.add(term.toString())
                    }
                }
                KeyTermAdapter.submitList(list)
                KeyTermAdapter.notifyDataSetChanged()
            } else {
                Log.d("Error", "Current data: null")
            }
        }
    }

    fun buttonAddKeyTermOnClick(view : View) {
        //TODO only allow them to add 10 key terms
        if (TextFieldKeyTerm.text.toString() == "") {
            TextFieldKeyTerm.error = "Please enter a key term"
        } else {
            val ref = database.collection("users").document(mAuth.uid.toString())
            ref.update("key_terms", FieldValue.arrayUnion(TextFieldKeyTerm.text.toString()))

            TextFieldKeyTerm.text?.clear()
            val hideKeyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hideKeyboard.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}