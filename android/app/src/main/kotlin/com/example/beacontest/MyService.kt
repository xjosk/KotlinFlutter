package com.example.beacontest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.NonNull
import androidx.core.app.NotificationCompat
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.BeaconTransmitter

class MyService() : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if(intent!!.getAction() == "start"){
                beaconTest()
                val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, "message")
                    .setContentText("Running in the background")
                    .setContentTitle("Beacontest")
                    .setSmallIcon(R.drawable.ic_android_black_24dp)
                startForeground(101, builder.build())
            }
            else if (intent!!.getAction() == "stop"){
                stopForeground(true);
                stopSelf(101);
            }

        }
        return START_NOT_STICKY
    }

    private fun beaconTest(): Int {
        val beacon = Beacon.Builder()
            .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")
            .setId2("1")
            .setId3("2")
            .setManufacturer(0x4c00)
            .setTxPower(-59)
            .setDataFields(arrayListOf(0L))
            .build()
        val beaconParser = BeaconParser()
            .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")
        val beaconTransmitter = BeaconTransmitter(applicationContext, beaconParser)
        beaconTransmitter.startAdvertising(beacon)
        val result = BeaconTransmitter.checkTransmissionSupported(this)

        return result
    }

    override fun onDestroy() {
        super.onDestroy()
        println("A")
    }

    override fun onBind(@NonNull p0: Intent?): IBinder? {

        return null
    }

}