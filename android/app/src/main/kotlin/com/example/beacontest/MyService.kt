package com.example.beacontest

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.NonNull
import androidx.core.app.NotificationCompat

class MyService() : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, "messages")
                .setContentText("Running in the background")
                .setContentTitle("Beacontest")
                .setSmallIcon(R.drawable.ic_android_black_24dp)
            startForeground(101, builder.build())
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(@NonNull p0: Intent?): IBinder? {

        return null
    }

}