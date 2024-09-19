package com.example.mealchain

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_page)

        val loginButton: TextView = findViewById(R.id.LoginButton)
        val storeUserDetail: Button = findViewById(R.id.ButtonSignUp)
        val emailEditText: EditText = findViewById(R.id.editTextTextEmailAddress)
        val passwordEditText: EditText = findViewById(R.id.editTextNumberPassword)

        // Store user details in SharedPreferences
        storeUserDetail.setOnClickListener {
            val username = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                val sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()

                // Save username and password
                editor.putString("Username", username)
                editor.putString("Password", password)
                editor.apply()

                Toast.makeText(this, "User SignUp Successfully", Toast.LENGTH_SHORT).show()

                // Optionally, navigate to another activity after saving
                val intent = Intent(this, LoginPage::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            }
        }

        // Navigate to login page when loginButton is clicked
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }
    }
}
