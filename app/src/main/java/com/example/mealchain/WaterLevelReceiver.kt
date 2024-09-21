package com.example.mealchain

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class WaterLevelReceiver : BroadcastReceiver() {

    private val CHANNEL_ID = "water_level_channel"
    private val NOTIFICATION_ID = 2

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("WaterLevelReceiver", "Checking water levels...")


        val waterPreferences = context.getSharedPreferences("WaterPreferences", Context.MODE_PRIVATE)
        val totalPreferences = context.getSharedPreferences("TotalPreferences", Context.MODE_PRIVATE)

        val savedLevel = waterPreferences.getLong("waterLevel", -1L)
        val totalWaterLevel = totalPreferences.getLong("totalWaterLevel", 0L)

        if (savedLevel != -1L && totalWaterLevel < savedLevel) {
            val difference = savedLevel - totalWaterLevel // Calculate the difference
            Log.d("WaterLevelReceiver", "Water level low: $totalWaterLevel ml vs $savedLevel ml")
            sendNotification(context, totalWaterLevel, savedLevel, difference)
        } else {
            Log.d("WaterLevelReceiver", "Water level is sufficient or goal not set.")
        }
    }

    private fun sendNotification(context: Context, totalWaterLevel: Long, savedLevel: Long, difference: Long) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.water_b__1_)
            .setContentTitle("Water Level Low")
            .setContentText("You're short by ${difference}ml. Total: $totalWaterLevel ml, Goal: $savedLevel ml.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }
}
