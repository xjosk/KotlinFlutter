import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Material App',
      home: Scaffold(
        appBar: AppBar(
          title: Text('Material App Bar'),
        ),
        body: const BeaconMain(),
      ),
    );
  }
}

class BeaconMain extends StatefulWidget {
  const BeaconMain({Key? key}) : super(key: key);

  @override
  State<BeaconMain> createState() => _BeaconMainState();
}

class _BeaconMainState extends State<BeaconMain> {
  static const platform = MethodChannel('samples.flutter.dev/beaconTest');

  Future<void> _testBeacon() async {
    try {
      final String result = await platform.invokeMethod('testBeacon');
      print(result);
    } on PlatformException catch (e) {
      print(e);
    }
  }

  Future<void> _startService() async {
    try {
      final String result = await platform.invokeMethod('startService');
    } on PlatformException catch (e) {
      print(e);
    }
  }

  Future<void> _stopService() async {
    try {
      final String result = await platform.invokeMethod('stopService');
    } on PlatformException catch (e) {
      print(e);
    }
  }

  Future<void> _startScanning() async {
    try {
      final String result = await platform.invokeMethod('startScanning');
    } on PlatformException catch (e) {
      print(e);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: Center(
      child: SizedBox(
          width: MediaQuery.of(context).size.width,
          child: Column(
            children: [
              TextButton(
                  onPressed: () async {
                    _startService();
                  },
                  child: const Text('Start service')),
              TextButton(
                  onPressed: () async {
                    _stopService();
                  },
                  child: const Text("Stop service")),
              TextButton(
                  onPressed: () async {
                    _startScanning();
                  },
                  child: const Text("Start Scanning")),
            ],
          )),
    ));
  }
}
