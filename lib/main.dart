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

  Future<void> _startService(
      {required String title, required String text}) async {
    try {
      final String result = await platform.invokeMethod('startService');
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
          child: TextButton(
              onPressed: () async {
                _startService(title: "hola", text: "desde flutter");

                while (true) {
                  await Future.delayed(const Duration(seconds: 5));
                  print("hi");
                }
              },
              child: const Text('Test'))),
    ));
  }
}
