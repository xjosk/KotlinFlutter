package com.example.beacontest

import android.util.Log
import androidx.lifecycle.Observer
import io.flutter.plugin.common.EventChannel
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.MonitorNotifier
import org.altbeacon.beacon.Region

class Observers {
    companion object{
        var eventSink: EventChannel.EventSink? = null
        var region: Region = Region("all-beacons", null, null, null)
        var TAG = "Observer"
        fun centralRangingObserver(): Observer<Collection<Beacon>> {
            return Observer<Collection<Beacon>> {
                Log.d(TAG, "Ranged: ${it.count()} beacons")
                Log.d(TAG, "IM EXECUTING TWICE XD")
                for (beacon: Beacon in it) {
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
        }
        fun centralMonitoringObserver(): Observer<Int> {
            return Observer<Int> {
                    state ->
                if (state == MonitorNotifier.OUTSIDE) {
                    Log.d(TAG, "outside beacon region: "+region)
                }
                else {
                    Log.d(TAG, "inside beacon region: "+region)
                }
            }
        }
    }
}