package com.example.news_aggregator.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.news_aggregator.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.content_main.top_app_bar

/**
 * Activity to create an account.
 * @property mAuth FirebaseAuth
 * @property database FirebaseFirestore
 */
class CreateAccountActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseFirestore

    /**
     * Initialize activity
     * @param savedInstanceState Bundle?
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        val toolbar = top_app_bar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    /**
     * Onclick function for when the user tries to create an account.
     * @param view View
     */
    fun buttonCreateAccountOnClick(view: View) {
        val email = text_field_email.editText?.text.toString()
        val password = Text_field_password.editText?.text.toString()
        if (email == "") {
            text_field_email.error = getString(R.string.account_blank_field)
        }
        if (password == "") {
            Text_field_password.error = getString(R.string.account_blank_field)
        }
        if (email != "" && password != "") {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val tempArr = ArrayList<String>()
                        val map = hashMapOf(
                            getString(R.string.firestore_key_terms) to tempArr
                        )
                        database.collection(getString(R.string.firestore_users))
                            .document(mAuth.uid.toString()).set(map)
                            .addOnSuccessListener {
                                updateUI()
                            }
                            .addOnFailureListener {
                                text_field_email.error = null
                                Text_field_password.error = getString(R.string.account_error)
                            }
                    } else {
                        text_field_email.error = null
                        val error = task.exception?.message
                        Text_field_password.error = error
                    }
                }
        }
    }

    /**
     * Go to the main activity if the user created an account.
     */
    private fun updateUI() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}