package com.example.beacontest

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.Region

class ServiceTest: Service() {
    var region: Region = Region("all-beacons", null, null, null)
    lateinit var beaconManager: BeaconManager

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        beaconManager = BeaconManager.getInstanceForApplication(this)
        setupForegroundService()
        return START_NOT_STICKY
    }

    private fun setupForegroundService() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val builder = Notification.Builder(this, "BeaconReferenceApp")
            builder.setSmallIcon(R.drawable.ic_android_black_24dp)
            builder.setContentTitle("Scanning for Beacons")
            val intent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT + PendingIntent.FLAG_IMMUTABLE
            )
            builder.setContentIntent(pendingIntent)

            builder.setChannelId("beacon-ref-notification-id");
            BeaconManager.getInstanceForApplication(this).enableForegroundServiceScanning(builder.build(), 456)
            BeaconManager.getInstanceForApplication(this).setEnableScheduledScanJobs(false)

            startForeground(456,builder.build())
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ON DESTROY","destroying.. idk")

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(beaconManager.foregroundServiceNotificationId)
        beaconManager.stopMonitoring(region)
        beaconManager.stopRangingBeacons(region)
        stopForeground(true)
        stopSelf()
        beaconManager.disableForegroundServiceScanning()

    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

}