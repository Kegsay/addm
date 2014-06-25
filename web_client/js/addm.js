
$(window).load(function(){
    $.ajax({
        type: 'GET',
        url: "http://localhost:8080/addm/devices",
        dataType: 'jsonp',
        success: function (data) {
            // move the device id to '_id' and dump entries into an array, and make a set of all the possible headers
            headerSet = {_id:true}
            results = []
            for (var key in data) {
                if (!data.hasOwnProperty(key)) { continue; }
                for (field in data[key]) {
                    headerSet[field] = true;
                }
                entry = data[key]
                entry._id = key
                results.push(entry)
            }
            
            console.log(JSON.stringify(results))
            
            // add in headers
            var th;
            for (var header in headerSet) {
                th = $('<th/>');
                th.append("<span>" + header + "</span>")
                $('#addmtablehead').append(th)
            }
            
            // add in rows
            var tr;
            for (var i = 0; i < results.length; i++) {
                tr = $('<tr/>');
                for (var header in headerSet) {
                    var cellValue = ""
                    if (header in results[i]) {
                        cellValue = results[i][header]
                    tr.append("<td>" + cellValue + "</td>");
                }
                $('#addmtablebody').append(tr);
            }
            
            $('#addmtable').tablesorter(); 
            }
        }
    });
});