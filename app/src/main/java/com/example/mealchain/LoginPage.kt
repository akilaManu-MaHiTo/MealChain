package com.example.mealchain

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        val emailEditText: EditText = findViewById(R.id.Email)
        val passwordEditText: EditText = findViewById(R.id.Password)
        val loginButton: Button = findViewById(R.id.buttonLogin)

        // Fetch stored user details from SharedPreferences
        val sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        val savedUsername = sharedPreferences.getString("Username", "")
        val savedPassword = sharedPreferences.getString("Password", "")

        // Set click listener for login button
        loginButton.setOnClickListener {
            val enteredUsername = emailEditText.text.toString()
            val enteredPassword = passwordEditText.text.toString()

            // Validate user input with stored credentials
            if (enteredUsername == savedUsername && enteredPassword == savedPassword) {
                // Login success, navigate to HomePage
                val intent = Intent(this, HomePage::class.java)
                startActivity(intent)
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
            } else {
                // Login failure, show error message
                Toast.makeText(this, "Invalid email or password!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
