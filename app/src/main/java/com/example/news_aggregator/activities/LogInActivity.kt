package com.example.news_aggregator.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.news_aggregator.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.top_app_bar


class LogInActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        val toolbar = top_app_bar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun buttonLogInOnClick(view: View) {
        val email = TextFieldEmail.editText?.text.toString()
        val password = TextFieldPassword.editText?.text.toString()
        if (email == "") {
            TextFieldEmail.error = "Field cannot be blank"
        }
        if (password == "") {
            TextFieldPassword.error = "Field cannot be blank"
        }
        if (password != "" && email != "") {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Success", "signInWithEmail:success")
                        updateUI()
                    } else {
                        Log.w("Error", "signInWithEmail:failure", task.exception)
                        TextFieldEmail.error = null
                        TextFieldPassword.error = "Password or email is incorrect"
                    }
                }
        }

    }

    private fun updateUI() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}