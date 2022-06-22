package com.example.beacontest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import io.flutter.app.FlutterApplication

class MyApplication: FlutterApplication() {

    val CHANNEL_ID = "autoStartServiceChannel"
    val CHANNEL_NAME = "Auto Start Service Channel"

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()

        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel("message","Messages", NotificationManager.IMPORTANCE_NONE)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
            Log.d("test_after_boot","ok")
        }*/
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel2 = NotificationChannel(
                "BeaconReferenceApp",
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager2 = getSystemService(
                NotificationManager::class.java
            )
            manager2.createNotificationChannel(serviceChannel2)

            val serviceChannel3 = NotificationChannel(
                "beacon",
                "a",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager3 = getSystemService(
                NotificationManager::class.java
            )
            manager3.createNotificationChannel(serviceChannel3)

        }
    }
}