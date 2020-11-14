package com.example.news_aggregator.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.news_aggregator.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun buttonCreateAccountOnClick(view: View) {
        val email = TextFieldEmail.editText?.text.toString()
        val password = TextFieldPassword.editText?.text.toString()
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Success", "createUserWithEmail:success")
                    val user = mAuth.currentUser
                    val tempArr = ArrayList<String>()
                    val map = hashMapOf(
                        "key_terms" to tempArr
                    )
                    database.collection("users").document(mAuth.uid.toString()).set(map)
                        .addOnSuccessListener { Log.d("Done", "DocumentSnapshot successfully written!") }
                        .addOnFailureListener { e -> Log.w("Error", "Error writing document", e) }
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Error", "createUserWithEmail:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    fun updateUI(user : FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}