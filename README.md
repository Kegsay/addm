addm
====

Android Development Device Monitor

Got lots of test devices but don't know where they are, if they are charged, which SIM is in them, etc? ADDM is a simple Android application which will periodically poke a url with information about the device.

This includes:
- Device model / manufacturer
- OS version
- Network information (which wifi network)
- SIM status + number
- Battery % and charging status
- External SD card status
- System uptime

This may later include:
- The google account logged into the device
- The applications installed on the device (package names, app names, version strings/numbers, debug/release signatures)
- A 'ping my device' feature which will make the device vibrate/make noises so it can be easily located. This will be done using GCM to avoid unnecessary battery drain.

The URL which is poked along with the update rate can be configured from the application. Data is sent as JSON via an HTTP PUT.
