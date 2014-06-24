#!/usr/bin/env python
import datetime
import time
import json
import sys
import signal
from threading import Thread
from BaseHTTPServer import HTTPServer, BaseHTTPRequestHandler
 
class PUTHandler(BaseHTTPRequestHandler):
    def do_PUT(self):
        print self.headers
        length = int(self.headers.getheader('Content-Length', 0))
        content = self.rfile.read(length)
        try:
            info = json.loads(content)
            info["server_ts"] = to_date(int(time.time()))
            if "uptime" in info:
                    info["uptime"] = to_hms(int(info["uptime"]/1000))
            if "wall_clock_time" in info:
                    info["wall_clock_time"] = to_date(int(info["wall_clock_time"]/1000))

            filename = get_filename(info)
            print "Filename %s with data %s" % (filename, info)
            with open(filename, 'w') as output:
                output.write(json.dumps(info))
            self.send_response(200)
        except Exception as e:
            print "Failed to parse PUT: %s" % e
            self.send_response(500)

def to_date(ts):
    return datetime.datetime.fromtimestamp(ts).strftime('%Y-%m-%d %H:%M:%S')

def to_hms(time_secs):
    mins, secs = divmod(time_secs, 60)
    hours, mins = divmod(mins, 60)
    return '%02dh %02dm %02ds' % (hours, mins, secs)

def get_filename(content):
    """ Get a suitable filename for this device poke. Typically
    this is a unique device ID.

    Args:
        content : The JSON object submitted from the device
    Returns:
        A suitable filename.
    """
    # just blob serial and secure if they exist.
    fname = ""
    if "serial" in content:
        fname += content["serial"]
        fname += "--"
    if "secure_android_id" in content:
        fname += content["secure_android_id"]

    if len(fname) == 0:
        # can't really store this then
        raise Exception("No valid device identifying keys")

    return fname + ".json"
 
def run_on(port):
    print("Starting a server on port %i" % port)
    server_address = ('0.0.0.0', port)
    httpd = HTTPServer(server_address, PUTHandler)
    httpd.serve_forever()
 
if __name__ == "__main__":
    port = int(sys.argv[1])
    server = Thread(target=run_on, args=[port])
    server.daemon = True
    server.start()
    signal.pause() # Wait for interrupt signal, e.g. KeyboardInterrupt. Fails on Windows
