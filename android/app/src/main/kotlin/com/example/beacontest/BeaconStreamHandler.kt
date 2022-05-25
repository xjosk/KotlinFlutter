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
    }

    override fun onCancel(arguments: Any?) {

    }
}