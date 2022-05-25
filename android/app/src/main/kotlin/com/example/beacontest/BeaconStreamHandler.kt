package com.example.beacontest

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.IBinder
import android.os.Parcelable
import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import io.flutter.plugin.common.EventChannel
import org.altbeacon.beacon.*
import org.altbeacon.beacon.service.BeaconService

class BeaconStreamHandler(private var activity: Activity?) :  EventChannel.StreamHandler{
    private var eventSink: EventChannel.EventSink? = null
    var region: Region = Region("all-beacons", null, null, null)
    lateinit var beaconManager: BeaconManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onListen(args: Any?, events: EventChannel.EventSink) {
        eventSink = events
        BeaconScan(context = activity?.baseContext, eventSink = eventSink)
        //startListeningToBeacons()
    }

    override fun onCancel(arguments: Any?) {
    }

    private fun startListeningToBeacons() {

        beaconManager = BeaconManager.getInstanceForApplication(activity?.baseContext!!) //activity?.let { BeaconManager.getInstanceForApplication(it.baseContext) }!!
        BeaconManager.setDebug(true)

        beaconManager.beaconParsers.clear()

        beaconManager.beaconParsers.add(
            BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")
        )

        var forService = Intent(activity, ServiceTest()::class.java)
        activity?.startService(forService)

        beaconManager.setEnableScheduledScanJobs(false)
        beaconManager.backgroundBetweenScanPeriod = 0
        beaconManager.backgroundScanPeriod = 1100

        beaconManager.startMonitoring(region)
        beaconManager.startRangingBeacons(region)

        val regionViewModel = BeaconManager.getInstanceForApplication(activity?.baseContext!!).getRegionViewModel(region)

        regionViewModel.regionState.observeForever( centralMonitoringObserver)

        regionViewModel.rangedBeacons.observeForever( centralRangingObserver)
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





    companion object {
        const val TAG = "BeaconReference"
    }


}