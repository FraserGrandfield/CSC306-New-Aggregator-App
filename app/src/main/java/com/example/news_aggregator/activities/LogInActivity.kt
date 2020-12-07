package com.example.news_aggregator.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.news_aggregator.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
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
    }

    fun buttonLogInOnClick(view: View) {
        val email = TextFieldEmail.editText?.text.toString()
        val password = TextFieldPassword.editText?.text.toString()
        if (email == "") {
            TextFieldEmail.error = getString(R.string.account_blank_field)
        }
        if (password == "") {
            TextFieldPassword.error = getString(R.string.account_blank_field)
        }
        if (password != "" && email != "") {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("Success", "signInWithEmail:success")
                        updateUI()
                    } else {
                        Log.w("Error", "signInWithEmail:failure", task.exception)
                        TextFieldEmail.error = null
                        TextFieldPassword.error = getString(R.string.account_incorrect_creds)
                    }
                }.addOnFailureListener {
                    view.let { Snackbar.make(it, getString(R.string.snackbar_login), Snackbar.LENGTH_LONG).show() }

                }
        }
    }

    private fun updateUI() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}