package com.example.beacontest

import android.app.Service
import android.content.Intent
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

        region = Region("all-beacon", null, null, null)
        beaconManager.startMonitoring(region)
        beaconManager.startRangingBeacons(region)

        val regionViewModel = BeaconManager.getInstanceForApplication(this).getRegionViewModel(region)

        regionViewModel.regionState.observeForever( centralMonitoringObserver)

        regionViewModel.rangedBeacons.observeForever( centralRangingObserver)

        return START_NOT_STICKY

    }

    val centralMonitoringObserver = Observer<Int> { state ->
        if (state == MonitorNotifier.OUTSIDE) {
            Log.d(TAG, "outside beacon region: "+region)
        }
        else {
            Log.d(TAG, "inside beacon region: "+region)
        }
    }

    val centralRangingObserver = Observer<Collection<Beacon>> { beacons ->
        Log.d(TAG, "Ranged: ${beacons.count()} beacons")
        for (beacon: Beacon in beacons) {
            Log.d(TAG, "$beacon about ${beacon.distance} meters away")
        }
    }

    companion object {
        val TAG = "BeaconReference"
    }

    override fun onBind(@NonNull p0: Intent?): IBinder? {
        return null
    }

}