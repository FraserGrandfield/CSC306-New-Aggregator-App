package com.example.news_aggregator.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.news_aggregator.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_main.top_app_bar

/**
 * Activity for logging in.
 * @property mAuth FirebaseAuth
 */
class LogInActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    /**
     * Initialize the activity.
     * @param savedInstanceState Bundle?
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        val toolbar = top_app_bar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    /**
     * On click for when the user tries to log in.
     * @param view View
     */
    fun buttonLogInOnClick(view: View) {
        val email = text_field_email.editText?.text.toString()
        val password = Text_field_password.editText?.text.toString()
        if (email == "") {
            text_field_email.error = getString(R.string.account_blank_field)
        }
        if (password == "") {
            Text_field_password.error = getString(R.string.account_blank_field)
        }
        if (password != "" && email != "") {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        updateUI()
                    } else {
                        text_field_email.error = null
                        Text_field_password.error = getString(R.string.account_incorrect_creds)
                    }
                }.addOnFailureListener {
                    view.let {
                        Snackbar.make(
                            it,
                            getString(R.string.snackbar_login),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    /**
     * User was able to login so go to main activity.
     */
    private fun updateUI() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}