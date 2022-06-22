// ignore_for_file: prefer_const_constructors

import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:shared_preferences/shared_preferences.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  Color primaryColor = const Color(0XFF1A1956);
  Color accentColor = const Color(0XFFFF9F68);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        debugShowCheckedModeBanner: false,
        theme: ThemeData(
            primaryColor: primaryColor,
            colorScheme:
                ColorScheme.fromSwatch().copyWith(secondary: accentColor),
            textButtonTheme: TextButtonThemeData(
                style: TextButton.styleFrom(
                    textStyle: TextStyle(fontSize: 20),
                    padding: EdgeInsets.symmetric(vertical: 14),
                    primary: Colors.white,
                    backgroundColor: primaryColor,
                    shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(25)))),
            outlinedButtonTheme: OutlinedButtonThemeData(
                style: TextButton.styleFrom(
                    textStyle: TextStyle(fontSize: 20),
                    padding: EdgeInsets.symmetric(vertical: 14),
                    primary: Colors.black,
                    backgroundColor: Colors.white,
                    shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(25)),
                    side: BorderSide(width: 4, color: primaryColor)))),
        title: 'SWIT',
        home: const BeaconMain());
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
  late StreamSubscription<dynamic> subscription;

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
    subscription =
        event_channel.receiveBroadcastStream().listen((dynamic event) {
      print('Received FLUTTER: $event');
      controller.add(event);
    }, onError: (dynamic error) {
      print('Received error FLUTTER: ${error.message}');
    });
  }

  stopListeningToBeacons() async {
    await subscription.cancel();
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
        body: Container(
      alignment: Alignment.center,
      decoration: BoxDecoration(
          gradient: LinearGradient(
              stops: const [.0, .65],
              begin: Alignment.bottomCenter,
              end: Alignment.topCenter,
              colors: [
                Theme.of(context).colorScheme.secondary,
                Theme.of(context).primaryColor
              ])),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.center,
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Expanded(
              flex: 4,
              child: Container(
                  alignment: Alignment.center,
                  child: SizedBox(
                      width: MediaQuery.of(context).size.width / 2,
                      height: MediaQuery.of(context).size.width / 2,
                      child: Image.asset('assets/icons/swit-logo.png')))),
          Expanded(
            flex: 4,
            child: IntrinsicWidth(
              stepWidth: MediaQuery.of(context).size.width * .75,
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  TextButton(
                      onPressed: _isPressed
                          ? null
                          : () async {
                              await listenToBeacons(beaconEventsController);
                              await setBool("isPressed", true);
                              bool isPressed = await getBool("isPressed");
                              setState(() {
                                _isPressed = isPressed;
                              });
                            },
                      child: const Text("Start scan")),
                  const SizedBox(height: 50),
                  OutlinedButton(
                      onPressed: !_isPressed
                          ? null
                          : () async {
                              await stopListeningToBeacons();
                              await _stopScanning();
                              await setBool("isPressed", false);
                              bool isPressed = await getBool("isPressed");
                              setState(() {
                                _isPressed = isPressed;
                              });
                            },
                      child: const Text("Stop scan"))
                ],
              ),
            ),
          )
        ],
      ),
    ));
  }
}
