
function msToTime(duration) {
    var seconds = parseInt((duration/1000)%60)
        , minutes = parseInt((duration/(1000*60))%60)
        , hours = parseInt((duration/(1000*60*60))%24)
        , days = parseInt((duration/(1000*60*60*24)));

    hours = (hours < 10) ? "0" + hours : hours;
    minutes = (minutes < 10) ? "0" + minutes : minutes;
    seconds = (seconds < 10) ? "0" + seconds : seconds;

    return (days > 0 ? days+"d " : "") + hours + "h " + minutes + "m " + seconds + "s";
}

$(window).load(function(){
    $.ajax({
        type: 'GET',
        url: "http://localhost:8080/addm/devices",
        dataType: 'jsonp',
        success: function (data) {
            // move the device id to '_id' and dump entries into an array, and make a set of all the possible headers
            headerSet = {_id:true};
            results = [];
            for (var key in data) {
                if (!data.hasOwnProperty(key)) { continue; }
                for (field in data[key]) { // add json keys as header names
                    headerSet[field] = true;
                }
                entry = data[key];
                entry._id = key;
                
                ms_to_hms_keys = [ "uptime" ];
                for (var ikey=0; ikey<ms_to_hms_keys.length; ikey++) {
                    mstohms_key = ms_to_hms_keys[ikey];
                    if (mstohms_key in entry) {
                        entry[mstohms_key] = msToTime(entry[mstohms_key]);
                    }
                }

                ts_to_date_keys = [ "wall_clock_time" , "server_ts" ];
                for (var ikey=0; ikey<ts_to_date_keys.length; ikey++) {
                    tstodate_key = ts_to_date_keys[ikey];
                    if (tstodate_key in entry) {
                        entry[tstodate_key] = new Date(entry[tstodate_key]).toLocaleString();
                    }
                }

                if ("serial" in entry && entry["serial"] in addm_serials) {
                    entry["serial"] = addm_serials[entry["serial"]];
                }
                
                results.push(entry);
            }
            
            console.log(JSON.stringify(results));
            
            // show only some of the headers instead of all of them and give them pretty names
            filteredHeaders = {build_manufacturer:"Manufacturer", 
                               build_model:"Model", 
                               serial:"Serial/Name",
                               server_ts:"Last seen", 
                               sim_state:"SIM State", 
                               battery_percent:"Battery %", 
                               battery_charge_source:"Charging", 
                               uptime:"Uptime", 
                               os_version:"OS" }
            
            // add in headers
            var th;
            for (var header in filteredHeaders) {
                th = $('<th/>');
                th.append("<span>" + filteredHeaders[header] + "</span>");
                $('#addmtablehead').append(th);
            }
            
            // add in rows
            var tr;
            for (var i = 0; i < results.length; i++) {
                tr = $('<tr/>');
                for (var header in filteredHeaders) {
                    var cellValue = "";
                    if (header in results[i]) {
                        cellValue = results[i][header];
                    }
                    tr.append("<td>" + cellValue + "</td>");
                }
                $('#addmtablebody').append(tr);
            }
            
            $('#addmtable').tablesorter(); 
        }
    });
});
