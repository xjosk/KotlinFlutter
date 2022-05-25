package com.example.beacontest

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import io.flutter.plugin.common.EventChannel
import org.altbeacon.beacon.*

@RequiresApi(Build.VERSION_CODES.O)
class BeaconScan (private val context: Context?, private val eventSink: EventChannel.EventSink?){
    companion object {
        var TAG = "BeaconScanObject"
    }
    var region: Region = Region("all-beacons", null, null, null)
    private lateinit var beaconManager: BeaconManager
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
        Log.d(TAG, "IM EXECUTING TWICE XD")
        for (beacon: Beacon in beacons) {
            Log.d(TAG, "Distancia del iBeacon: *********** ${beacon.distance} ****************")
            Log.d(TAG, "UUID: *********** ${beacon.id1} ****************")
            Log.d(TAG, "Major: *********** ${beacon.id2} ****************")
            Log.d(TAG, "Minor: *********** ${beacon.id3} ****************")
            Log.d(TAG, "Fuerza de señal: *********** ${beacon.rssi} ****************")
            Log.d(TAG, "Potencia de transmision: *********** ${beacon.txPower} ****************")
            Log.d(TAG, "Buelotooth Address: *********** ${beacon.bluetoothAddress} ****************")

            eventSink?.success(beacon.toString())
        }
    }

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

        if(!regionViewModel.regionState.hasActiveObservers() && !regionViewModel.rangedBeacons.hasActiveObservers()) {
            regionViewModel.regionState.observeForever(centralMonitoringObserver)
            regionViewModel.rangedBeacons.observeForever(centralRangingObserver)
        }

        ContextCompat.startForegroundService(context, Intent(context, ServiceTest::class.java))
    }


}