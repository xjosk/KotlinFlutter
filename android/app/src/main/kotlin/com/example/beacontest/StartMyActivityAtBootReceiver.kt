package com.example.beacontest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

private const val TAG = "MyBroadcastReceiver"

class StartMyActivityAtBootReceiver: BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("FlutterSharedPreferences",Context.MODE_PRIVATE)
        val isPressed = sharedPreferences.getBoolean("flutter.isPressed", false)

        if(isPressed) {
            BeaconScan(context = context)
        }
        Log.d(TAG,"hello")
    }
}