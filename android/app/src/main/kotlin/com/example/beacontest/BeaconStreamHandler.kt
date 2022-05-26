package com.example.beacontest

import android.app.*
import android.os.Build
import androidx.annotation.RequiresApi
import io.flutter.plugin.common.EventChannel

class BeaconStreamHandler(private var activity: Activity?) :  EventChannel.StreamHandler{

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onListen(args: Any?, events: EventChannel.EventSink) {
        Observers.eventSink = events
        startScanningBeacon()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startScanningBeacon() {
        BeaconScan(context = activity)
    }

    override fun onCancel(arguments: Any?) {

    }
}