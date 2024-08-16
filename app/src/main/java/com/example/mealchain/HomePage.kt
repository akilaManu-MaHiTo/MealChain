package com.example.mealchain

import android.content.Intent
import android.health.connect.datatypes.BodyWaterMassRecord
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class HomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onboard)

        // Initialize the ConstraintLayout and set the OnClickListener inside onCreate
        val breakfastLayout: ConstraintLayout = findViewById(R.id.breakfast_button)
        breakfastLayout.setOnClickListener {
            // Intent to navigate to Breakfast activity
            val intent = Intent(this, Breakfast::class.java)
            startActivity(intent)
        }

        val water: Button = findViewById(R.id.waterBtn)
        water.setOnClickListener {
            // Intent to navigate to Breakfast activity
            val intent = Intent(this, Water::class.java)
            startActivity(intent)
        }

        val activity: Button = findViewById(R.id.activityId)
        activity.setOnClickListener {
            // Intent to navigate to Breakfast activity
            val intent = Intent(this, Activity::class.java)
            startActivity(intent)
        }

        val analyzeBtn: Button = findViewById(R.id.analyzeBtn)
        analyzeBtn.setOnClickListener {
            // Intent to navigate to Breakfast activity
            val intent = Intent(this, Analyze::class.java)
            startActivity(intent)
        }

        val lunchBtn: ConstraintLayout = findViewById(R.id.lunchBtn)
        lunchBtn.setOnClickListener {
            // Intent to navigate to Breakfast activity
            val intent = Intent(this, Lunch::class.java)
            startActivity(intent)
        }

        val dinnerBtn: ConstraintLayout = findViewById(R.id.dinnerBtn)
        dinnerBtn.setOnClickListener {
            // Intent to navigate to Breakfast activity
            val intent = Intent(this, Dinner::class.java)
            startActivity(intent)
        }

        val profile: ImageView = findViewById(R.id.profileBtn)
        profile.setOnClickListener {
            // Intent to navigate to Breakfast activity
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }


    }
}
