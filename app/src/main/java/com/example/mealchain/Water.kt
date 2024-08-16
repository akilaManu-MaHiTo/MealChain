package com.example.mealchain

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Water : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.water_page)

        val meal: Button = findViewById(R.id.mealBtn)
        meal.setOnClickListener {
            // Intent to navigate to Breakfast activity
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }

        val activity: Button = findViewById(R.id.activityBtn1)
        activity.setOnClickListener {
            // Intent to navigate to Breakfast activity
            val intent = Intent(this, Activity::class.java)
            startActivity(intent)
        }

    }


}
