""" Simple REST web server using web.py """
import json
import sqlite3
import sys
import web

class putdevice:
    """ Handler for /addm/device/<device id> """
    def PUT(self, device_id):
        # this will throw if the data isn't valid json
        device_data = json.loads(web.data())
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
        
        web.header("Content-Type", "application/json")
        return json.dumps(devices_json)
        
        
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
    con = sqlite3.connect(dbname)
    con.execute('CREATE TABLE IF NOT EXISTS devices(id TEXT PRIMARY KEY, data TEXT, UNIQUE(id) ON CONFLICT REPLACE);')
    return web.database(dbn='sqlite', db=dbname)
        
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
    