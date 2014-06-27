
function msToTime(duration) {
    var seconds = parseInt((duration/1000)%60)
        , minutes = parseInt((duration/(1000*60))%60)
        , hours = parseInt((duration/(1000*60*60))%24);

    hours = (hours < 10) ? "0" + hours : hours;
    minutes = (minutes < 10) ? "0" + minutes : minutes;
    seconds = (seconds < 10) ? "0" + seconds : seconds;

    return hours + "h " + minutes + "m " + seconds + "s";
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
                for (field in data[key]) {
                    headerSet[field] = true;
                }
                entry = data[key];
                entry._id = key;
                
                ms_to_hms_keys = [ "uptime" ]
                for (var ikey=0; ikey<ms_to_hms_keys.length; ikey++) {
                    mstohms_key = ms_to_hms_keys[ikey]
                    if (mstohms_key in entry) {
                        entry[mstohms_key] = msToTime(entry[mstohms_key]);
                    }
                }

                ts_to_date_keys = [ "wall_clock_time" , "server_ts" ]
                for (var ikey=0; ikey<ts_to_date_keys.length; ikey++) {
                    tstodate_key = ts_to_date_keys[ikey]
                    if (tstodate_key in entry) {
                        entry[tstodate_key] = new Date(entry[tstodate_key]).toString();
                    }
                }
                
                results.push(entry);
            }
            
            console.log(JSON.stringify(results));
            
            // add in headers
            var th;
            for (var header in headerSet) {
                th = $('<th/>');
                th.append("<span>" + header + "</span>");
                $('#addmtablehead').append(th);
            }
            
            // add in rows
            var tr;
            for (var i = 0; i < results.length; i++) {
                tr = $('<tr/>');
                for (var header in headerSet) {
                    var cellValue = "";
                    if (header in results[i]) {
                        cellValue = results[i][header];
                    tr.append("<td>" + cellValue + "</td>");
                }
                $('#addmtablebody').append(tr);
            }
            
            $('#addmtable').tablesorter(); 
            }
        }
    });
});
