var analysis_data;
var sampleFlag;
var analysis_name;
    
var colorBG = [
    'rgba(23, 162, 184, 0.3)',
    'rgba(40, 167, 69, 0.3)',
    'rgba(255, 193, 7, 0.3)',
    'rgba(220, 53, 69, 0.3)'
];
var colorBorder = [
    'rgba(23, 162, 184, 0.8)',
    'rgba(40, 167, 69, 0.8)',
    'rgba(255, 193, 7, 0.8)',
    'rgba(220, 53, 69, 0.8)'
];

$(document).ready(function(){
    try {
        analysis_data = JSON.parse(localStorage.getItem('analysis_data'));
        sampleFlag = JSON.parse(localStorage.getItem('sample'));
        username = JSON.parse(localStorage.getItem('username'));
        analysis_name = JSON.parse(localStorage.getItem('analysis_name'));
    } catch (error) {
        console.log(error)
        window.location.href = "index.html"
    }
    
    if(sampleFlag == true){
        document.getElementById("sample-clear").innerHTML = '<p id="reg-now"> REGISTER TO RECEIVE FULL FUNCTIONALITY </p>';
        document.getElementById("link-home").setAttribute("href","index.html");
        document.getElementById("prev_shared").innerHTML="";
        document.getElementById("thread-dump-id").innerHTML="Sample";
    }else{
        document.getElementById("display-name").innerHTML = username;
    }
    
    if(analysis_data != null){
        display_analysis(analysis_data);
        
    }else{
        window.location.href = "index.html"
        document.getElementById("thread-dump-title").innerHTML = 'ERROR IN DUMP FILE : CANNOT ANALYZE';
    }
    if(analysis_name != null){
        document.getElementById("thread-dump-id").innerHTML=analysis_name;
    }

    showShared('shared_analysis');
    showShared('old_analysis');
});
function showShared(sidebarItem){
    let shared = JSON.parse(localStorage.getItem(sidebarItem));
    let sharing="";
    for (let index = 0; index <3&&index<shared.length; index++) {
        let share = shared[index];
        
        sharing=sharing+
        `<li class="nav-item">
          <a class="nav-link first-link" href="">
            <i class="fas fa-fw fa-table"></i>
            <span>${share['name']}</span></a>
        </li>`
        
    } 
    if(sidebarItem=="shared_analysis"){
        document.getElementById("shared").innerHTML=sharing;
    }
    else{
        document.getElementById("previous").innerHTML=sharing;
    } 
    
}
function display_analysis(jsonData){
    var state_vise = jsonData.state_vise;
    var deamons = jsonData.deamons;
    var stack_length = jsonData.stack_length;
    var syncMap = jsonData.sync_map;
    var threadMap = jsonData.thread_map;
    var identicle_stack_report = jsonData.identicle_stack_report;
    var identicle_stack_map = jsonData.identicle_stack_map;

    // console.log(identicle_stack_report)

    display_statevise(state_vise, threadMap);
    display_deamons(deamons);
    display_slength(stack_length);
    display_culprit_report(syncMap);
    display_identicle_stack(identicle_stack_report, identicle_stack_map, threadMap)
}

function display_statevise(state_vise, threadMap){
    var running = state_vise["running"];
    var waiting = state_vise["awaiting notification"];
    var blocked = state_vise["waiting to acquire"];
    var non_java = state_vise["non-Java thread"];

    document.getElementById("running-threads").innerHTML = running.length;
    document.getElementById("waiting-threads").innerHTML = waiting.length;
    document.getElementById("blocked-threads").innerHTML = blocked.length;
    document.getElementById("non-java-threads").innerHTML = non_java.length;
    document.getElementById("total-threads").innerHTML = running.length + waiting.length + blocked.length + non_java.length;

    var data1 = [non_java.length, running.length, waiting.length, blocked.length];
    var label1 = ['Non-Java Threads', 'Running', 'Waiting', 'Blocked'];
    drawPieChart("myChart", data1, label1, colorBG, colorBorder);

    function threadsByStateTable(state, array){
        var threads = '';
        array.forEach(threadId => {
            threads = threads + threadMap[threadId]["name"] + ", ";
        });
        threads = threads.substring(0, threads.length - 2);

        var inner = document.getElementById("state-details").innerHTML;
        var html = 
            '<tr>'+
                '<td class="font-weight-bold text-gray-700">'+ state +'</td>'+
                '<td>'+ threads +'</td>'+
            '</tr>';

        document.getElementById("state-details").innerHTML = inner + html;
    }

    threadsByStateTable('RUNNING', running);
    threadsByStateTable('WAITING', waiting);
    threadsByStateTable('BLOCKED', blocked);
}

function display_deamons(deamons){
    var deamon = deamons["daemon"];
    var non_deamon = deamons["undefined"];

    document.getElementById("deamon-threads").innerHTML = deamon.length;
    document.getElementById("non-deamon-threads").innerHTML = non_deamon.length;

    var deamon_percent = Math.round((deamon.length / (deamon.length + non_deamon.length))*100)

    document.getElementById("deamon-percent").innerHTML = deamon_percent;
    document.getElementById("non-deamon-percent").innerHTML = (100 - deamon_percent);

    $('#deamon-progress').attr('aria-valuenow', deamon_percent).css('width', (String(deamon_percent)+ "%"));

    // Math.round(other_data * 100) / 100
}

function display_slength(stack_length){
    var slength_less10 = [];
    var slength_to100 = [];
    var slength_to500 = [];
    var slength_more500 = [];

    if(stack_length.hasOwnProperty("slength_less10")){
        slength_less10 = stack_length["slength_less10"];
    }

    if(stack_length.hasOwnProperty("slength_to100")){
        slength_to100 = stack_length["slength_to100"];
    }

    if(stack_length.hasOwnProperty("slength_to500")){
        slength_to500 = stack_length["slength_to500"];
    }

    if(stack_length.hasOwnProperty("slength_more500")){
        slength_more500 = stack_length["slength_more500"];
    }

    document.getElementById("slength_less10").innerHTML = slength_less10.length;
    document.getElementById("slength_to100").innerHTML = slength_to100.length;
    document.getElementById("slength_to500").innerHTML = slength_to500.length;
    document.getElementById("slength_more500").innerHTML = slength_more500.length;

    var data2 = [slength_less10.length, slength_to100.length, slength_to500.length, slength_more500.length];
    var label2 = ['<10 lines', '10 - 100 lines', '100 - 500 lines', '>500 lines'];
    drawPieChart("myChart2", data2, label2, colorBG, colorBorder);
}

function display_culprit_report(syncMap){
    let keys = Object.keys(syncMap);
    
    keys.forEach(element => {
        var currentSync = syncMap[element];
        var inner = document.getElementById("sync-details").innerHTML;
        var details;

        if(currentSync.hasOwnProperty("lockHolder")){
            var lockHolder = currentSync["lockHolder"];
            details = '<p class="detail-parah"> <span class ="font-weight-bold text-gray-700"> Held By : </span>'+ lockHolder["name"] + '</p>' +
                      '<p class="detail-parah"> <span class ="font-weight-bold text-gray-700"> Helder\'s Status : </span> <span class="text-capitalize">'+ lockHolder["getStatus"] + '</span></p>';
        
            var heldLocks = lockHolder["locksHeld"];
            if(heldLocks.length>1){
                details = details + '<p class="detail-parah"> <span class ="font-weight-bold text-gray-700"> Other Locks Held : </span>';
                heldLocks.forEach(index => {
                    if(index != currentSync["id"]){
                        details = details + syncMap[index]["className"] +', ';        
                    }
                });
                details = details.substring(0, details.length - 2) + '</p>';
            } 
        }

        var notificationWaiters = currentSync["notificationWaiters"];
        var waitingOn = [];
        if(notificationWaiters.length != 0){
            if(details != undefined){
                details = details + '<p class="detail-parah"> <span class ="font-weight-bold text-gray-700"> Waiting for Notifiction From : </span>';
            }else{
                details = '<p class="detail-parah"> <span class ="font-weight-bold text-gray-700"> Waiting for Notifiction From : </span>';
            }
            
            notificationWaiters.forEach(waiter => {
                if(!(waitingOn.includes(waiter["name"]))){
                    waitingOn.push(waiter["name"])
                }
            });

            waitingOn.forEach(waiter => {
                details = details + waiter +', '; 
            });

            details = details.substring(0, details.length - 2) + '</p>';
        }

        var lockWaiters = currentSync["lockWaiters"];
        var waitingThreads = [];
        if(lockWaiters.length != 0){
            if(details != undefined){
                details = details + '<p class="detail-parah"> <span class ="font-weight-bold text-gray-700"> Threads being Locked : </span>';
            }else{
                details = '<p class="detail-parah"> <span class ="font-weight-bold text-gray-700"> Threads being Locked : </span>';
            }
            
            lockWaiters.forEach(waiter => {
                if(!(waitingThreads.includes(waiter["name"]))){
                    waitingThreads.push(waiter["name"])
                }
            });

            waitingThreads.forEach(waiter => {
                details = details + waiter +', '; 
            });

            details = details.substring(0, details.length - 2); + '</p>';
        }

        if(currentSync["deadlockStatus"] != 'NONE'){
            renderd3tree(currentSync["lockHolder"]["name"], waitingThreads);
        }

        var html = 
        '<tr>'+
            '<td>'+ currentSync["id"] +'</td>'+
            '<td>'+ currentSync["className"] +'</td>'+
            '<td>'+ currentSync["deadlockStatus"] +'</td>'+
            '<td>'+ details + '</td>'+
        '</tr>';

        document.getElementById("sync-details").innerHTML = inner + html;
    });
}

function display_identicle_stack(identicle_stack_report, identicle_stack_map, threadMap){
    let keys = Object.keys(identicle_stack_report);
    var unidenticle_count = 0;
    var unidenticle ="";
    var empty_count = 0;
    var data = [];
    var labels = [];

    keys.forEach(stackId => {
        var identicle_stacks = identicle_stack_report[stackId];
        var frames = identicle_stack_map[stackId];
        var threads = "";
        var stackTrace = "";

        if(identicle_stacks.length > 1 && stackId!="EMPTY"){
            identicle_stacks.forEach(threadId => {
                var status = getStatus(threadId);
                threads = threads + '<li>' + threadMap[threadId]["name"] + ' - <span class="text-capitalize">' + status + '</span> </li>';
            });

            threads = threads.substring(0, threads.length - 2);
            
            frames.forEach(frame => {
                stackTrace = stackTrace + '<p>' + frame + '</p>';
            });

            stackTrace = stackTrace.substring(0, stackTrace.length - 2);

            var inner = document.getElementById("identicle-details").innerHTML;
            var html = 
                '<tr>'+
                    '<td class="font-weight-bold text-gray-700">'+ stackId +'</td>'+
                    '<td>'+ stackTrace +'</td>'+
                    '<td>'+ threads +'</td>'+
                '</tr>';
            
            document.getElementById("identicle-details").innerHTML = inner + html;

            data.push(identicle_stacks.length);
            labels.push(stackId);

        }else if(stackId=="EMPTY"){
            identicle_stacks.forEach(threadId => {
                var status = getStatus(threadId);
                threads = threads + '<li>' + threadMap[threadId]["name"] + ' - <span class="text-capitalize">' + status + '</span> </li>';
            });

            threads = threads.substring(0, threads.length - 2);

            var inner = document.getElementById("identicle-details").innerHTML;
            var html = 
                '<tr>'+
                    '<td class="font-weight-bold text-gray-700">'+ stackId +'</td>'+
                    '<td> [No StackTrace] </td>'+
                    '<td>'+ threads +'</td>'+
                '</tr>';
            
            document.getElementById("identicle-details").innerHTML = inner + html;

            empty_count = identicle_stacks.length;

        }else{
            unidenticle_count++;
            var status = getStatus(identicle_stacks[0]);
            unidenticle = unidenticle + '<li>' + threadMap[identicle_stacks[0]]["name"] + ' - <span class="text-capitalize">' + status + '</span> </li>';
        }
    });

    var inner = document.getElementById("identicle-details").innerHTML;
    var html = 
        '<tr>'+
            '<td class="font-weight-bold text-gray-700"> UNIDENTICLE </td>'+
            '<td> [Unidenticle StackTrace] </td>'+
            '<td>'+ unidenticle +'</td>'+
        '</tr>';
    
    document.getElementById("identicle-details").innerHTML = inner + html;

    data.push(empty_count);
    labels.push("EMPTY");

    data.push(unidenticle_count);
    labels.push("UNIDENTICLE");

    function getStatus(id){
        var status = threadMap[id]["getStatus"];
        switch (status) {
            case "waiting to acquire":
                status = "blocked"
                break;
            case "awaiting notification":
                status = "waiting"
                break;
        
            default:
                break;
        }
        return status;
    }

    drawBarChart("myChart3", data, labels, 'rgba(36, 17, 106, 0.3)', 'rgba(36, 17, 106, 0.8)');
}

