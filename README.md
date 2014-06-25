Android Development Device Monitor
==================================

Got lots of test devices but don't know where they are, if they are charged, which SIM is in them, etc? ADDM is a simple Android application which will periodically poke a URL with information about the device.

Client
------
The client is an APK which can be installed on test devices. It will run on startup and periodically do an HTTP PUT to the in-app configured URL.

The PUT includes the following data:
- Device serial and secure ID
- Device model / manufacturer
- OS version
- Network information (mobile/wifi data, 3G/4G/SSID)
- SIM status + number
- Battery % and charging status
- External SD card status [TODO]
- System uptime and wall clock time

This may later include:
- The google account logged into the device
- The applications installed on the device (package names, app names, version strings/numbers, debug/release signatures)
- Location of the device (cached / coarse / fine, configurable to reduce battery drain)
- A 'ping my device' feature which will make the device vibrate/make noises so it can be easily located. This will be done using GCM to avoid unnecessary battery drain. This feature will need to be configured before use.

The base URL which is poked along with the update rate can be configured from the application. Data is sent as JSON via an HTTP PUT to [base url]/[device serial]--[device secure id]

Server [TODO]
------
ADDM also includes a Django web server to service PUTs and provide a web interface for accessing information about devices. This makes it easy to get ADDM working usefully straight out the box.

