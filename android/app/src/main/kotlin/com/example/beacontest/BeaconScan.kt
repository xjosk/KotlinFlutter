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
    var region: Region = Region("all-beacons", null, null, null)
    private lateinit var beaconManager: BeaconManager
    val centralMonitoringObserver = Observer<Int> { state ->
        if (state == MonitorNotifier.OUTSIDE) {
            Log.d(BeaconStreamHandler.TAG, "outside beacon region: "+region)
        }
        else {
            Log.d(BeaconStreamHandler.TAG, "inside beacon region: "+region)
        }
    }

    val centralRangingObserver = Observer<Collection<Beacon>> { beacons ->

        Log.d(BeaconStreamHandler.TAG, "Ranged: ${beacons.count()} beacons")
        for (beacon: Beacon in beacons) {
            Log.d(BeaconStreamHandler.TAG, "Distancia del iBeacon: *********** ${beacon.distance} ****************")
            Log.d(BeaconStreamHandler.TAG, "UUID: *********** ${beacon.id1} ****************")
            Log.d(BeaconStreamHandler.TAG, "Major: *********** ${beacon.id2} ****************")
            Log.d(BeaconStreamHandler.TAG, "Minor: *********** ${beacon.id3} ****************")
            Log.d(BeaconStreamHandler.TAG, "Fuerza de señal: *********** ${beacon.rssi} ****************")
            Log.d(BeaconStreamHandler.TAG, "Potencia de transmision: *********** ${beacon.txPower} ****************")
            Log.d(BeaconStreamHandler.TAG, "Buelotooth Address: *********** ${beacon.bluetoothAddress} ****************")

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

        regionViewModel.regionState.observeForever(centralMonitoringObserver)

        regionViewModel.rangedBeacons.observeForever(centralRangingObserver)

        ContextCompat.startForegroundService(context, Intent(context, ServiceTest::class.java))


    }


}