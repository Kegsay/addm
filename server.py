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
            print info
            self.send_response(200)
        except Exception as e:
            print "Failed to parse PUT: %s" % e
        self.send_response(500)
 
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