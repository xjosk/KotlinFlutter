package com.example.beacontest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import org.altbeacon.beacon.*

private const val TAG = "MyBroadcastReceiver"

class StartMyActivityAtBootReceiver: BroadcastReceiver() {

    var region: Region = Region("all-beacons", null, null, null)
    private lateinit var beaconManager: BeaconManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("FlutterSharedPreferences",Context.MODE_PRIVATE)
        val isPressed = sharedPreferences.getBoolean("flutter.isPressed", false)

        if(isPressed) {
            BeaconScan(context = context, eventSink = null)
        }
        /*if(false) {
            beaconManager = BeaconManager.getInstanceForApplication(context)

            BeaconManager.setDebug(true)

            beaconManager.beaconParsers.clear()

            beaconManager.beaconParsers.add(
                BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")
            )

            var forService = Intent(context, ServiceTest()::class.java)
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
        }*/
        Log.d(TAG,"hello")





    }
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

        }
    }
}