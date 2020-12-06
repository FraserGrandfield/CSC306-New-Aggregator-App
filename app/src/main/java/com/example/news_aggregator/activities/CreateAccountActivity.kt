package com.example.news_aggregator.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.news_aggregator.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.content_main.top_app_bar

class CreateAccountActivity : AppCompatActivity()  {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        val toolbar = top_app_bar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    fun buttonCreateAccountOnClick(view: View) {
        val email = TextFieldEmail.editText?.text.toString()
        val password = TextFieldPassword.editText?.text.toString()
        if (email == "") {
            TextFieldEmail.error = "Field cannot be blank"
        }
        if (password == "") {
            TextFieldPassword.error = "Field cannot be blank"
        }
        if (email != "" && password != "") {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Success", "createUserWithEmail:success")
                        val tempArr = ArrayList<String>()
                        val map = hashMapOf(
                            "key_terms" to tempArr
                        )
                        database.collection("users").document(mAuth.uid.toString()).set(map)
                            .addOnSuccessListener {
                                Log.d("Done", "DocumentSnapshot successfully written!")
                                updateUI()
                            }
                            .addOnFailureListener { e -> Log.w("Error", "Error writing document", e) }
                    } else {
                        Log.w("Error", "createUserWithEmail:failure", task.exception)
                        TextFieldEmail.error = null
                        val error = task.exception?.message
                        TextFieldPassword.error = error
                    }
                }
        }
    }

    private fun updateUI() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}