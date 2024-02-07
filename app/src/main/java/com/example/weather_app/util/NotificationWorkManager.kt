package com.example.weather_app.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.weather_app.R
import com.example.weather_app.ui.home.HomeActivity
import java.util.Calendar
import java.util.concurrent.TimeUnit

class NotificationWorkManager(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        val weatherData = inputData.getStringArray("weatherData")

        Log.d("NotificationWorkManager", weatherData.contentToString())

        val weather = weatherData?.get(0) ?: "조회 실패"
        val minTemp = weatherData?.get(1) ?: ""
        val maxTemp = weatherData?.get(2) ?: ""
        val nx = weatherData?.get(3) ?: "55"
        val ny = weatherData?.get(4) ?: "127"

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationChannelId = "weather"
        val notificationChannelName = "Weather Channel"
        val notificationId = 1 // 고유한 ID로 설정

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(notificationChannelId, notificationChannelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val intent = Intent(applicationContext, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(applicationContext, notificationChannelId)
            .setContentTitle("오늘의 날씨 : $weather")
            .setContentText("최고 : ${maxTemp}° 최저 : ${minTemp}°")
            .setSmallIcon(R.drawable.ic_sun_and_cloud)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        notificationManager.notify(notificationId, notificationBuilder.build())

        val outputData : Data = workDataOf(
            "nx" to nx,
            "ny" to ny
        )

        //아침 7시 알림 후 다시 다음 날 아침 7시 WorkManager 실행
        repeatWorkManager(outputData)

        return Result.success()
    }

    //새로운 WorkManager를 생성하면서 매일 아침7시 계속 반복
    private fun repeatWorkManager(data : Data) {
        //제약조건
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        //Background 작업을 위한 WorkManager (Notification)
        val retrofitWorkManager = OneTimeWorkRequestBuilder<RetrofitWorkManager>()
            .setInputData(data)
            .setConstraints(constraints)
            .setInitialDelay(getCertainTime(), TimeUnit.MILLISECONDS)   //해당 시간만큼 딜레이
            .build()

        val notificationWorkManager = OneTimeWorkRequestBuilder<NotificationWorkManager>().build()

        //Retrofit WorkManager 실행 후 날씨 데이터 Notification WorkManager로 전달 및 정해진 시간에 알림 실행
        WorkManager.getInstance(applicationContext)
            .beginUniqueWork("work", ExistingWorkPolicy.REPLACE, retrofitWorkManager)
            .then(notificationWorkManager)
            .enqueue()
    }

    //아침 7시까지 남은 시간 계산 함수
    private fun getCertainTime(): Long {
        val currentDate = Calendar.getInstance()

        val dueDate = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 7)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        if (dueDate.before(currentDate))
            dueDate.add(Calendar.HOUR_OF_DAY, 24)

        return dueDate.timeInMillis - currentDate.timeInMillis
    }
}