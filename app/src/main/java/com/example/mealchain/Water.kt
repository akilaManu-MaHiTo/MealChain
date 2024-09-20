package com.example.mealchain

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class Water : AppCompatActivity() {

    // Constants
    private val MAX_WATER_LEVEL_IN_MILLILITERS = 5000 // Max value for the ProgressBar (5 liters)
    private val CHANNEL_ID = "water_level_channel"
    private val NOTIFICATION_ID = 1
    private val CHECK_INTERVAL = 10 * 1000L // 2 hours in milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.water_page)

        // Set up the Spinner with water levels
        val waterLevels = listOf("1L", "2L", "3L", "4L", "5L")
        val spinner: Spinner = findViewById(R.id.addWater)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, waterLevels)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Button to save the selected water level
        val addWaterLevelButton: Button = findViewById(R.id.AddWaterLevel)
        addWaterLevelButton.setOnClickListener {
            Log.d("Water", "AddWaterLevel button clicked")
            saveWaterLevel(spinner.selectedItem.toString())
        }

        // Clear All Data Button
        val clearDataButton: Button = findViewById(R.id.cleardata)
        clearDataButton.setOnClickListener {
            clearAllData()
        }

        // Set up multiple ConstraintLayouts dynamically
        val constraintLayoutIds = listOf(
            R.id.constraintLayoutWater1,
            R.id.constraintLayoutWater2,
            R.id.constraintLayoutWater3,
            R.id.constraintLayoutWater4,
            R.id.constraintLayoutWater5
        )

        val constraintLayouts = constraintLayoutIds.map { id ->
            findViewById<ConstraintLayout>(id)
        }

        for (layout in constraintLayouts) {
            layout?.setOnClickListener {
                Log.d("Water", "Layout clicked: ${layout.id}")
                handleLayoutClick(layout)
            }
        }

        // Initialize the water level display and ProgressBar
        updateWaterLevelDisplay()

        // Create the notification channel (for Android O and above)
        createNotificationChannel()

        // Schedule water level check every 2 hours
        scheduleWaterLevelCheck()
    }

    // Method to save the selected water level in SharedPreferences
    private fun saveWaterLevel(selectedLevelStr: String) {
        val selectedLevel = selectedLevelStr.replace("L", "").toLong() * 1000 // Convert liters to milliliters

        val sharedPreferences = getSharedPreferences("WaterPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putLong("waterLevel", selectedLevel)
        val isSaved = editor.commit()

        Log.d("Water", "Water level saved: $selectedLevel ml. Save successful: $isSaved")
        Toast.makeText(this, "Water level saved: $selectedLevel ml", Toast.LENGTH_SHORT).show()

        // Update the display and ProgressBar
        updateWaterLevelDisplay()
    }

    // Method to clear all SharedPreferences data
    private fun clearAllData() {
        val waterPreferences = getSharedPreferences("WaterPreferences", Context.MODE_PRIVATE)
        val totalPreferences = getSharedPreferences("TotalPreferences", Context.MODE_PRIVATE)

        // Clear data from both preferences
        waterPreferences.edit().clear().apply()
        totalPreferences.edit().clear().apply()

        // Update the UI after clearing data
        updateWaterLevelDisplay()

        Toast.makeText(this, "All data cleared", Toast.LENGTH_SHORT).show()
        Log.d("Water", "All shared preferences data cleared")
    }

    // Other existing methods (scheduleWaterLevelCheck, handleLayoutClick, etc.)
    // ...

    private fun scheduleWaterLevelCheck() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, WaterLevelReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Set repeating alarm to check water level every 2 hours
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            CHECK_INTERVAL,
            pendingIntent
        )
    }

    private fun handleLayoutClick(layout: ConstraintLayout) {
        // Retrieve the saved water level from SharedPreferences
        val waterPreferences = getSharedPreferences("WaterPreferences", Context.MODE_PRIVATE)
        val savedLevel = waterPreferences.getLong("waterLevel", 0L) // Default to 0 if not set

        // If the saved water level is 0, show a toast and return early
        if (savedLevel == 0L) {
            Toast.makeText(this, "First you need to add a water goal", Toast.LENGTH_SHORT).show()
            return
        }

        // Determine which layout was clicked and extract its water level
        val waterLevelTextView: TextView = when (layout.id) {
            R.id.constraintLayoutWater1 -> findViewById(R.id.newWater1)
            R.id.constraintLayoutWater2 -> findViewById(R.id.newWater2)
            R.id.constraintLayoutWater3 -> findViewById(R.id.newWater3)
            R.id.constraintLayoutWater4 -> findViewById(R.id.newWater4)
            R.id.constraintLayoutWater5 -> findViewById(R.id.newWater5)
            else -> throw IllegalArgumentException("Unknown layout ID")
        }

        val waterLevelValue = extractWaterLevel(waterLevelTextView.text.toString())

        // Retrieve the current total water level from SharedPreferences
        val totalPreferences = getSharedPreferences("TotalPreferences", Context.MODE_PRIVATE)
        val currentTotal = totalPreferences.getLong("totalWaterLevel", 0L)
        val newTotal = currentTotal + waterLevelValue

        // Save the updated total water level back to SharedPreferences
        val totalEditor = totalPreferences.edit()
        totalEditor.putLong("totalWaterLevel", newTotal)
        val isTotalSaved = totalEditor.commit()

        Log.d("Water", "Total water level updated: $newTotal ml. Save successful: $isTotalSaved")
        Toast.makeText(this, "Total water level updated: $newTotal ml", Toast.LENGTH_SHORT).show()

        // Update the display and ProgressBar
        updateWaterLevelDisplay()

        // Check if the user has reached their water goal
        checkGoalCompletion(newTotal)
    }


    private fun extractWaterLevel(waterLevelText: String): Long {
        return try {
            val valueString = waterLevelText.replace("ml", "").replace("L", "").trim()
            valueString.toLong()
        } catch (e: NumberFormatException) {
            Log.e("Water", "Error parsing water level: $waterLevelText", e)
            0L
        }
    }

    private fun updateWaterLevelDisplay() {
        val totalPreferences = getSharedPreferences("TotalPreferences", Context.MODE_PRIVATE)
        val totalWaterLevel = totalPreferences.getLong("totalWaterLevel", 0L)

        val waterPreferences = getSharedPreferences("WaterPreferences", Context.MODE_PRIVATE)
        val savedLevel = waterPreferences.getLong("waterLevel", -1L)

        val totalWaterLevelInLiters = totalWaterLevel / 1000.0

        val savedWaterLevelText: TextView = findViewById(R.id.savedWaterLevelText)
        savedWaterLevelText.text = if (savedLevel != -1L) {
            "%.2f L / %d L".format(totalWaterLevelInLiters, savedLevel / 1000)
        } else {
            "%.2f L / %d L".format(totalWaterLevelInLiters, savedLevel / 1000)
        }

        val progressBar: ProgressBar = findViewById(R.id.progressBar2)
        val progressPercentage = (totalWaterLevel * 100) / MAX_WATER_LEVEL_IN_MILLILITERS
        Log.d("Water", "Progress percentage: $progressPercentage")
        progressBar.progress = progressPercentage.toInt()
    }

    private fun checkGoalCompletion(totalWaterLevel: Long) {
        val waterPreferences = getSharedPreferences("WaterPreferences", Context.MODE_PRIVATE)
        val savedLevel = waterPreferences.getLong("waterLevel", -1L)

        if (savedLevel != -1L && totalWaterLevel >= savedLevel) {
            sendNotification()
        }
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val name = "Water Level Channel"
            val descriptionText = "Channel for water level notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.water_b__1_)
            .setContentTitle("You Reached Your Water Level Goal")
            .setContentText("Congratulations!!!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }
}
