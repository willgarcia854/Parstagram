package com.example.parstagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.ParseUser

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Check if there's a user logged in
        if (ParseUser.getCurrentUser() != null) {
            // take to MainActivity
            goToMainActivity()
        }

        findViewById<Button>(R.id.login_button).setOnClickListener{
            val username = findViewById<EditText>(R.id.edit_username).text.toString()
            val password = findViewById<EditText>(R.id.edit_password).text.toString()
            loginUser(username, password)
        }
        findViewById<Button>(R.id.signupBtn).setOnClickListener{
            val username = findViewById<EditText>(R.id.edit_username).text.toString()
            val password = findViewById<EditText>(R.id.edit_password).text.toString()
            signUpUser(username, password)
        }
    }

    private fun signUpUser(username: String, password: String){
        // Create the ParseUser
        val user = ParseUser()

        // Set fields for the user to be created
        user.setUsername(username)
        user.setPassword(password)

        user.signUpInBackground { e ->
            if (e == null) {
                // User has successfully created a new account
                // TODO: Navigate the user to the MainActivity
                goToMainActivity()
                // TODO: Show a toast to indicate the user successfully signed up for an account
                 Toast.makeText(this, "Successfully created an account", Toast.LENGTH_SHORT).show()
            } else {
                // Sign up didn't succeed
                e.printStackTrace()
                Toast.makeText(this, "Unable to create account", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(username: String, password: String){
        ParseUser.logInInBackground(
            username, password, ({ user, e ->
                if (user != null) {
                    // Hooray!  The user is logged in.
                    Log.i(TAG, "Successfully logged in!")
                    goToMainActivity()
                } else {
                    // Signup failed.  Look at the ParseException to see what happened.
                    e.printStackTrace()
                    Toast.makeText(this, "Error Logging in", Toast.LENGTH_SHORT).show()
                }
            })
        )
    }

    private fun goToMainActivity(){
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()


    }

    companion object{
        val TAG = "LoginActivity"
    }
}