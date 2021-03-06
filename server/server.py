""" Simple REST web server using web.py """
import datetime
import json
import os
import sqlite3
import sys
import time
import web

class putdevice:
    """ Handler for /addm/device/<device id> """
    def PUT(self, device_id):
        # this will throw if the data isn't valid json
        device_data = json.loads(web.data())
        device_data["server_ts"] = to_date(int(time.time()))
        insert_device_data(device_id, device_data)

class getdevices:
    """ Handler for /addm/devices """
    def GET(self):
        data = web.addm_db.select('devices')
        
        # device_id is the key to the top level json object, e.g.
        # { 
        #   "devid1" : {"data":"123","stuff":"here"},
        #   "devid2" : {"data":"987","stuff":"here"}
        # }
        devices_json = json.loads('{}')
        for row in data:
            devices_json[row["id"]] = json.loads(row["data"])
            
        # check for JSONP requests
        web.header("Content-Type", "application/json")
        if "callback" in web.input():
            return web.input()["callback"] + "(" + json.dumps(devices_json) + ")"
        else:
            return json.dumps(devices_json)
        
def to_date(ts):
    return datetime.datetime.fromtimestamp(ts).strftime('%Y-%m-%d %H:%M:%S')
        
def insert_device_data(dev_id, dev_data):
    """ INSERTs device data into the database.
    
    Args:
        dev_id : The device ID
        dev_data : The device data JSON object
    """
    # don't bother validating json keys; would rather not update the server
    # every time keys are added/removed.
    web.addm_db.insert('devices', id=dev_id, data=json.dumps(dev_data))
    

        
def setup_db():
    """ Creates a sqlite3 db if one doesn't exist and hooks web.py to it.
    
    Returns:
        The web.py db instance
    """
    dbname = 'addm.db'

    script_folder = os.sep.join(os.path.realpath(__file__).split(os.sep)[:-1]) # snip off filename
    data_folder = os.path.join(script_folder, "data") # subdir of script folder
    database_abs_path = os.path.join(data_folder, dbname)
    print "Database located at %s" % database_abs_path
    con = sqlite3.connect(database_abs_path)
    con.execute('CREATE TABLE IF NOT EXISTS devices(id TEXT PRIMARY KEY, data TEXT, UNIQUE(id) ON CONFLICT REPLACE);')
    return web.database(dbn='sqlite', db=database_abs_path)
        
def main():
    db = setup_db()
    web.addm_db = db
    
    # configure and run the server
    urls =  (
        '/addm/device/(.*)', 'putdevice',
        '/addm/devices', 'getdevices'
    )
    app = web.application(urls, globals())
    app.run()
    

if __name__ == '__main__':
    main()
    
