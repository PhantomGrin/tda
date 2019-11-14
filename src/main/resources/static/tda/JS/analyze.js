var username;

$(document).ready(function(){
    try {
        getAnalysis().then(data=>{
            oldAnalysis = JSON.parse(localStorage.getItem('old_analysis'));
        document.getElementById("display-name").innerHTML = username;
        displayPreviousAnalysis(oldAnalysis);
        $('#old-analysis').DataTable(
            {
              "lengthChange": false,
              "pageLength": 5
            }
        );
        retrieveTeamMembers();
        })
        username = JSON.parse(localStorage.getItem('username'));
        
        
        
    } catch (error) {
        console.log("Username not set")
        document.getElementById("display-name").innerHTML = "Undefined";
    }
});

function displayPreviousAnalysis(array){
    
    array.forEach(threadDumpObject => {
        try {
            var inner = document.getElementById("old-analysis-tbody").innerHTML;
            var html = 
                '<tr>'+
                    '<td class="id-data">'+ threadDumpObject["threadId"] +'</td>'+
                    '<td>'+ threadDumpObject["name"] +'</td>'+
                    '<td>'+ threadDumpObject["analysisDate"] +'</td>'+
                    '<td>'+ threadDumpObject["date"] + '</td>'+
                    '<td class="button-group-wrapper justify-content-center justify-content-md-center">'+
                        '<div class="joption-button-group ustify-content-center justify-content-md-center">'+
                            '<button class="option-buttons view-analysis-button" data-toggle="tooltip" data-placement="top" title="View Analysis"><i class="fas fa-external-link-alt"></i></button>'+
                            '<button class="option-buttons share-analysis-button" data-toggle="tooltip" data-placement="top" title="Share Analysis" data-toggle="modal" data-target="#exampleModal"><i class="fas fa-share-alt"></i></button>'+
                            '<button class="option-buttons delete-analysis-button" data-toggle="tooltip" data-placement="top" title="Delete Analysis"><i class="fas fa-trash"></i></button>'+
                        '</div>'+
                    '</td>'+
                '</tr>';

            document.getElementById("old-analysis-tbody").innerHTML = inner + html;
            
        } catch (error) {
            console.log(error)
        }
        
    });
}

$("body").on("click", ".view-analysis-button", function(){
    var value = $(this).closest("tr").find('td')[0].innerHTML;
    var name = $(this).closest("tr").find('td')[1].innerHTML;
    getSelectedAnalysis(value, name);
});

$("body").on("click", ".delete-analysis-button", function(){
    var value = $(this).closest("tr").find('td')[0].innerHTML;
    deleteAnalysis(value);
});

$("body").on("click", ".share-analysis-button", function(){
    var value = $(this).closest("tr").find('td')[0].innerHTML;
    localStorage.setItem("selected_dump", JSON.stringify(value));

    $($(this).data("target")).modal("show");
    var team = JSON.parse(localStorage.getItem('team_members'));

    displayTeam(team);
});

function deleteAnalysis(id){
    const url = serverURL +`/delete?id=${id}`;
    var token = JSON.parse(localStorage.getItem('token'));

    console.log(token['token']);
    
    fetch(url, {
        method:"GET",
        headers: {
            "Authorization": "Bearer "+ token['token']
        }
    })
    .then(response => {
        if (response.ok) {
           getAnalysis().then(data=>{
            oldAnalysis=JSON.parse(localStorage.getItem('old_analysis'));
            document.getElementById("old-analysis-tbody").innerHTML="";
            displayPreviousAnalysis(oldAnalysis);
            
           })  
           return  response;
        } else{
            throw new Error('Something went wrong');
        }
       
    })
    .catch(error => console.log(error))
}

function getSelectedAnalysis(id, name){
    const url = serverURL +`/getresult?id=${id}`;
    var token = JSON.parse(localStorage.getItem('token'));
    localStorage.setItem("analysis_name", JSON.stringify(name));

    console.log(token['token']);
    
    fetch(url, {
        method:"GET",
        headers: {
            "Authorization": "Bearer "+ token['token']
        }
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else{
            throw new Error('Something went wrong');
        }
    })
    .then(data => {
        console.log(data);
        localStorage.setItem("analysis_data", JSON.stringify(data));
        window.location.href = "dashboard.html";
    })
    .catch(error => console.error(error))
}

function retrieveTeamMembers(){
    const url = serverURL + `/team`;
    var token = JSON.parse(localStorage.getItem('token'));
    
    fetch(url, {
        method:"GET",
        headers: {
            "Authorization": "Bearer "+ token['token']
        }
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else{
            throw new Error('Something went wrong');
        }
    })
    .then(data => {
        localStorage.setItem("team_members", JSON.stringify(data));
    })
    .catch(error => console.error(error))
}

function displayTeam(array){
    array.forEach(teamMemember => {
        try {
            if(teamMemember["username"]!=username){
                var inner = document.getElementById("dropdown-content").innerHTML;
                var html = 
                '<li class="dropdown-item" data="'+ teamMemember["username"] +'">'+
                    '<span class="font-weight-bold member-name">'+ teamMemember["username"] +'</span> '+
                    '<span class="display-username"> &lt;'+ teamMemember["Firstname"] +'&gt; </span> '+
                '</li>';

                document.getElementById("dropdown-content").innerHTML = inner + html;
            }            
        } catch (error) {
            console.log(error)
        }
        
    });
}

$("body").on("click", ".dropdown-item", function(){
    var value = $(this).attr('data');
    document.getElementById("success-alert").classList.add("hidden-alert");
    document.getElementById("btn-choice").innerHTML = value;
});

$("#modal-share-btn").click(function (e){
    e.preventDefault();
    
    var token = JSON.parse(localStorage.getItem('token'));
    var dumpId = JSON.parse(localStorage.getItem('selected_dump'));
    var sharedMember = document.getElementById("btn-choice").innerHTML;

    const url = `http://localhost:8080/share?username=${sharedMember}&id=${dumpId}`;
    
    fetch(url, {
        method:"GET",
        headers: {
            "Authorization": "Bearer "+ token['token']
        }
    })
    .then(response => {
        if (response.ok) {
            document.getElementById("success-alert").classList.remove("hidden-alert");
            return response.json();
        } else{
            throw new Error('Something went wrong');
        }
    })
    .then(data => {
        console.log(data);
        localStorage.removeItem("selected_dump")
    })
    .catch(error => {
        console.error(error)
        document.getElementById("success-alert").classList.remove("alert-success");
        document.getElementById("success-alert").classList.add("alert-danger");
        document.getElementById("success-alert").classList.remove("hidden-alert");
        document.getElementById("message").innerHTML = error;
    })
});

$("#success-close").click(function(e){
    e.preventDefault();
    document.getElementById("success-alert").classList.add("hidden-alert");
});