package com.example.beacontest

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import io.flutter.plugin.common.EventChannel
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.MonitorNotifier
import org.altbeacon.beacon.Region

class Observers {
    companion object{
        var eventSink: EventChannel.EventSink? = null
        var region: Region = Region("all-beacons", null, null, null)
        private var TAG = "Observer"
        fun centralRangingObserver(context: Context): Observer<Collection<Beacon>> {
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


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val builder = NotificationCompat.Builder(context, "beacon")
                            .setSmallIcon(R.drawable.ic_android_black_24dp)
                            .setContentTitle("Beacon detected")
                            .setContentText(beacon.distance.toString())
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                        with(NotificationManagerCompat.from(context)) {
                            notify(123, builder.build())
                        }
                    }

                    eventSink?.success(beacon.toString())
                }
            }
        }
        fun centralMonitoringObserver(): Observer<Int> {
            return Observer<Int> {
                    state ->
                if (state == MonitorNotifier.OUTSIDE) {
                    Log.d(TAG, "outside beacon region: $region")
                }
                else {
                    Log.d(TAG, "inside beacon region: $region")
                }
            }
        }
    }
}