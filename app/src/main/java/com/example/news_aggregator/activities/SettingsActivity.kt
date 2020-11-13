package com.example.news_aggregator.activities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news_aggregator.R
import com.example.news_aggregator.adapters.KeyTermRecyclerAdapter
import com.example.news_aggregator.models.DataBaseModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.content_main.top_app_bar
import kotlinx.android.synthetic.main.fragment_for_you.recycler_view

class SettingsActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var KeyTermAdapter: KeyTermRecyclerAdapter
    private var list = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        mAuth = FirebaseAuth.getInstance()
        val database : FirebaseDatabase = FirebaseDatabase.getInstance()
        val ref = database.getReference("users/${mAuth.uid}/key_terms")
        val toolbar = top_app_bar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this.context)
            KeyTermAdapter = KeyTermRecyclerAdapter()
            adapter = KeyTermAdapter
        }
        val keyTermListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                val keyTerms = dataSnapshot.value
                if (keyTerms != null) {
                    for ((key, value) in keyTerms as HashMap<*, *>) {
                        if (value.toString() != "@anchor") {
                            list.add(value.toString())
                        }
                    }
                    KeyTermAdapter.submitList(list)
                    KeyTermAdapter.notifyDataSetChanged()
                    Log.e("snapshot", keyTerms.toString())
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        ref.addValueEventListener(keyTermListener)
    }

    fun buttonAddKeyTermOnClick(view : View) {
        mAuth.uid?.let { DataBaseModel.addKeyTerm(it, TextFieldKeyTerm.text.toString()) }
        TextFieldKeyTerm.text?.clear()
        val hideKeyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        hideKeyboard.hideSoftInputFromWindow(view.windowToken, 0)
    }

}