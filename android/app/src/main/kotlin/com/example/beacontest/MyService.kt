package com.example.beacontest

import android.app.Service
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.BeaconTransmitter


class MyService() : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if(intent!!.getAction() == "start"){
                beaconTest(intent!!.getAction())
                val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, "message")
                    .setContentText("Running in the background")
                    .setContentTitle("Beacontest")
                    .setSmallIcon(R.drawable.ic_android_black_24dp)
                startForeground(101, builder.build())
            }
            else if (intent!!.getAction() == "stop"){
                beaconTest(intent!!.getAction())
                stopForeground(true);
                stopSelf(101);
            }

        }
        return START_NOT_STICKY
    }

    private fun beaconTest(transmitting: String?) {
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

        if(transmitting == "start"){
            beaconTransmitter.startAdvertising(beacon, @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            object : AdvertiseCallback() {
                override fun onStartFailure(errorCode: Int) {
                    Log.e("RunTest", "Failure: $errorCode")
                }

                override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
                    Log.e("RunTest", "Success... $settingsInEffect")
                }
            })
        }

        Handler().postDelayed(Runnable {
            if (beaconTransmitter.isStarted) {
                Log.e("RunTest", "STOPPED...")
                beaconTransmitter.stopAdvertising()
            }
        }, 2000)

    }

    override fun onDestroy() {
        super.onDestroy()
        println("A")
    }

    override fun onBind(@NonNull p0: Intent?): IBinder? {

        return null
    }

}