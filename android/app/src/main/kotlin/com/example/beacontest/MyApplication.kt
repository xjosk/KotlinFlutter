package com.example.beacontest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
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
            val serviceChannel = NotificationChannel(
                "beacon-ref-notification-id",
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)

            val serviceChannel2 = NotificationChannel(
                "BeaconReferenceApp",
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager2 = getSystemService(
                NotificationManager::class.java
            )
            manager2.createNotificationChannel(serviceChannel2)
        }
    }
}