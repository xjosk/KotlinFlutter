package com.example.beacontest

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.Observer
import org.altbeacon.beacon.*

class BeaconService : Service(){

    lateinit var region: Region

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val beaconManager = BeaconManager.getInstanceForApplication(this)
        BeaconManager.setDebug(true)

        beaconManager.beaconParsers.clear()

        beaconManager.beaconParsers.add(
            BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")
        )

        setupForegroundService()
        beaconManager.setEnableScheduledScanJobs(false)
        beaconManager.backgroundBetweenScanPeriod = 0
        beaconManager.backgroundScanPeriod = 1100

        region = Region("all-beacons", null, null, null)
        beaconManager.startMonitoring(region)
        beaconManager.startRangingBeacons(region)

        val regionViewModel = BeaconManager.getInstanceForApplication(this).getRegionViewModel(region)

        regionViewModel.regionState.observeForever( centralMonitoringObserver)

        regionViewModel.rangedBeacons.observeForever( centralRangingObserver)

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
            builder.setContentIntent(pendingIntent);
            val channel =  NotificationChannel("beacon-ref-notification-id",
                "My Notification Name", NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "My Notification Channel Description"
            val notificationManager =  getSystemService(
                Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channel.getId());
            BeaconManager.getInstanceForApplication(this).enableForegroundServiceScanning(builder.build(), 456)
        }

    }

    private val centralMonitoringObserver = Observer<Int> { state ->
        if (state == MonitorNotifier.OUTSIDE) {
            Log.d(TAG, "outside beacon region: "+region)
        }
        else {
            Log.d(TAG, "inside beacon region: "+region)
        }
    }

    private val centralRangingObserver = Observer<Collection<Beacon>> { beacons ->
        Log.d(TAG, "Ranged: ${beacons.count()} beacons")
        for (beacon: Beacon in beacons) {
            Log.d(TAG, "$beacon about ${beacon.distance} meters away")
        }
    }

    companion object {
        const val TAG = "BeaconReference"
    }

    override fun onBind(@NonNull p0: Intent?): IBinder? {
        return null
    }

}