package com.example.beacontest

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import org.altbeacon.beacon.*

@RequiresApi(Build.VERSION_CODES.O)
class BeaconScan (private val context: Context?) {
    companion object {
        var TAG = "BeaconScanObject"
    }
    var region: Region = Observers.region
    private lateinit var beaconManager: BeaconManager

    init {
        startScanningBeacon()
    }

    private fun startScanningBeacon() {


        beaconManager = BeaconManager.getInstanceForApplication(context!!)

        BeaconManager.setDebug(true)

        beaconManager.beaconParsers.clear()

        beaconManager.beaconParsers.add(
            BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")
        )

        val forService = Intent(context, ServiceTest()::class.java)
        context.startForegroundService(forService)

        beaconManager.setEnableScheduledScanJobs(false)
        beaconManager.backgroundBetweenScanPeriod = 0
        beaconManager.backgroundScanPeriod = 1100

        beaconManager.startMonitoring(region)
        beaconManager.startRangingBeacons(region)

        val regionViewModel =
            BeaconManager.getInstanceForApplication(context).getRegionViewModel(region)

        if(!regionViewModel.rangedBeacons.hasObservers() && !regionViewModel.rangedBeacons.hasObservers()) {
            regionViewModel.regionState.observeForever(Observers.centralMonitoringObserver())
            regionViewModel.rangedBeacons.observeForever(Observers.centralRangingObserver(context))
        }

    }


}