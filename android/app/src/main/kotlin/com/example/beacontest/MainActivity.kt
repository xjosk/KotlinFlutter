package com.example.beacontest

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.BeaconTransmitter


class MainActivity: FlutterActivity() {
    private var forService: Intent? = null
    private val CHANNEL = "samples.flutter.dev/beaconTest"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GeneratedPluginRegistrant.registerWith(FlutterEngine(this))

    }

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
            // Note: this method is invoked on the main thread.
                call, result ->
            when (call.method) {
                "testBeacon" -> {
                    val beaconResponse = beaconTest()

                    result.success(beaconResponse.toString())
                }
                "startService" -> {
                    forService = Intent(this@MainActivity, MyService()::class.java)

                    val serviceResponse = startService()

                    result.success(serviceResponse.toString())
                }
            }
        }
    }

    private fun startService() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startForegroundService(forService)
        }
        else {
            startService(forService)
        }
    }

    private fun beaconTest(): Int {
        val beacon = Beacon.Builder()
            .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")
            .setId2("1")
            .setId3("2")
            .setManufacturer(0x4c00)
            .setTxPower(-59)
            .setDataFields(arrayListOf(0L))
            .build()
        val beaconParser = BeaconParser()
            .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")
        val beaconTransmitter = BeaconTransmitter(applicationContext, beaconParser)
        beaconTransmitter.startAdvertising(beacon)
        val result = BeaconTransmitter.checkTransmissionSupported(context)

        return result
    }

}
