import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:shared_preferences/shared_preferences.dart';

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
  static const EventChannel event_channel = EventChannel('listen_beacon');
  final StreamController<String> beaconEventsController =
      StreamController<String>.broadcast();

  // Future<void> _testBeacon() async {
  //   try {
  //     final String result = await platform.invokeMethod('testBeacon');
  //     print(result);
  //   } on PlatformException catch (e) {
  //     print(e);
  //   }
  // }

  // Future<void> _startService() async {
  //   try {
  //     final String result = await platform.invokeMethod('startService');
  //   } on PlatformException catch (e) {
  //     print(e);
  //   }
  // }

  // Future<void> _stopService() async {
  //   try {
  //     final String result = await platform.invokeMethod('stopService');
  //   } on PlatformException catch (e) {
  //     print(e);
  //   }
  // }

  // Future<void> _startScanning() async {
  //   try {
  //     final String result = await platform.invokeMethod('startScanning');
  //   } on PlatformException catch (e) {
  //     print(e);
  //   }
  // }

  Future<void> _stopScanning() async {
    try {
      final String result = await platform.invokeMethod('stopScanning');
    } on PlatformException catch (e) {
      print(e);
    }
  }

  // Future<void> _testSharedPreferences() async {
  //   try {
  //     await platform.invokeMethod('sharedPreferences');
  //   } on PlatformException catch (e) {
  //     print(e);
  //   }
  // }

  listenToBeacons(StreamController controller) async {
    event_channel.receiveBroadcastStream().listen((dynamic event) {
      print('Received FLUTTER: $event');
      controller.add(event);
    }, onError: (dynamic error) {
      print('Received error FLUTTER: ${error.message}');
    });
  }

  setBool(String key, bool value) async {
    SharedPreferences myPrefs = await SharedPreferences.getInstance();
    myPrefs.setBool(key, value);
  }

  Future<bool> getBool(String key) async {
    SharedPreferences myPrefs = await SharedPreferences.getInstance();
    return myPrefs.getBool(key) ?? false;
  }

  bool _isPressed = false;

  void checkIsPressed() async {
    bool isPressed = await getBool("isPressed");

    setState(() {
      _isPressed = isPressed;
    });

    if (_isPressed) {
      await _stopScanning();
      await listenToBeacons(beaconEventsController);
    }
  }

  @override
  void initState() {
    super.initState();
    checkIsPressed();
  }

  @override
  void dispose() {
    beaconEventsController.close();
    super.dispose();
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
                  onPressed: _isPressed
                      ? null
                      : () async {
                          await listenToBeacons(beaconEventsController);
                          await setBool("isPressed", true);
                          setState(() {
                            _isPressed = true;
                          });
                        },
                  child: const Text("START SCANNING AND GET INFO")),
              TextButton(
                  onPressed: !_isPressed
                      ? null
                      : () async {
                          await _stopScanning();
                          await setBool("isPressed", false);
                          setState(() {
                            _isPressed = false;
                          });
                        },
                  child: const Text("Stop scanning"))
            ],
          )),
    ));
  }
}
