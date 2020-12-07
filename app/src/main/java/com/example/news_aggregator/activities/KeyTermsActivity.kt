package com.example.news_aggregator.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news_aggregator.R
import com.example.news_aggregator.adapters.KeyTermRecyclerAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_key_terms.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.top_app_bar

/**
 * Activity for key terms.
 * @property mAuth FirebaseAuth
 * @property database FirebaseFirestore
 * @property keyTermAdapter KeyTermRecyclerAdapter
 * @property list ArrayList<String>
 */
class KeyTermsActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var keyTermAdapter: KeyTermRecyclerAdapter
    private var list = ArrayList<String>()

    /**
     * Initialize the activity. Get the key terms from FireStore and display them.
     * @param savedInstanceState Bundle?
     */
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
            keyTermAdapter = KeyTermRecyclerAdapter()
            adapter = keyTermAdapter
        }

        val ref =
            database.collection(getString(R.string.firestore_users)).document(mAuth.uid.toString())
        //Get the key terms from the database.
        ref.addSnapshotListener { snapshot, e ->
            if (e != null) {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.snackbar_cannot_get_key_terms),
                        Snackbar.LENGTH_LONG
                    ).show()
            }
            if (snapshot != null && snapshot.exists()) {
                list.clear()
                if (snapshot.data != null) {
                    val keyTerms =
                        snapshot.data?.get(getString(R.string.firestore_key_terms)) as ArrayList<*>
                    for (term in keyTerms) {
                        list.add(term.toString())
                    }
                }
                keyTermAdapter.submitList(list)
                keyTermAdapter.notifyDataSetChanged()
            }
        }
    }

    /**
     * On click function for when the user tries to add a key term.
     * @param view View
     */
    fun buttonAddKeyTermOnClick(view: View) {
        if (list.size < 10) {
            if (text_field_key_term.text.toString() == "") {
                text_field_key_term.error = getString(R.string.key_term_blank_field)
            } else {
                val ref = database.collection(getString(R.string.firestore_users))
                    .document(mAuth.uid.toString())
                ref.update(
                    getString(R.string.firestore_key_terms),
                    FieldValue.arrayUnion(text_field_key_term.text.toString())
                ).addOnFailureListener {
                    view.let {
                        Snackbar.make(
                            it,
                            getString(R.string.snackbar_key_terms),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
                text_field_key_term.text?.clear()
                val hideKeyboard =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                hideKeyboard.hideSoftInputFromWindow(view.windowToken, 0)
            }
        } else {
            text_field_key_term.error = getString(R.string.key_terms_max_terms)
        }
    }
}