package com.example.mealchain

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page)

        val water: Button = findViewById(R.id.waterbtn1)
        water.setOnClickListener {
            // Intent to navigate to Breakfast activity
            val intent = Intent(this, Water::class.java)
            startActivity(intent)
        }

        val meal: Button = findViewById(R.id.mealBtn1)
        meal.setOnClickListener {
            // Intent to navigate to Breakfast activity
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }

    }


}
