package com.example.weather_app.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weather_app.R

class NotificationWorkManager(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        val weatherData = inputData.getStringArray("weatherData")

        Log.d("NotificationWorkManager", "${weatherData?.get(0)}, ${weatherData?.get(1)}, ${weatherData?.get(2)}")

        val weather = weatherData?.get(0)
        val minTemp = weatherData?.get(1)
        val maxTemp = weatherData?.get(2)

        // 백그라운드 작업 수행 후 Notification 생성
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationChannelId = "weather"
        val notificationChannelName = "Weather Channel"
        val notificationId = 1 // 고유한 ID로 설정

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(notificationChannelId, notificationChannelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notificationBuilder = NotificationCompat.Builder(applicationContext, notificationChannelId)
            .setContentTitle("오늘의 날씨 : $weather")
            .setContentText("최고 : ${maxTemp}° 최저 : ${minTemp}°")
            .setSmallIcon(R.drawable.ic_sun_and_cloud)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(notificationId, notificationBuilder.build())

        return Result.success()
    }
}