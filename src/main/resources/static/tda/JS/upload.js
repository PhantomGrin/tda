var username;

$(document).ready(function(){
    try {
        username = JSON.parse(localStorage.getItem('username'));
        document.getElementById("display-name").innerHTML = username;
        getUserDetails();
        getAnalysis();
        getSharedAnalysis();

    } catch (error) {
        console.log("Username not set")
        document.getElementById("display-name").innerHTML = "Undefined";
    }
});

function getUserDetails(){
    const url = serverURL + "/user";
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
        localStorage.setItem("user_details", JSON.stringify(data));
    })
    .catch(error => console.error(error))
}

function getSharedAnalysis(){
    const url = serverURL + "/shared";
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
        console.log(data);
        localStorage.setItem("shared_analysis", JSON.stringify(data));
    })
    .catch(error => console.error(error))
}

$("#chose-dump").click(function(e) {
    e.preventDefault();
    $("#dump-file").click();
});

$("#change-dump").click(function(e) {
    e.preventDefault();
    $("#dump-file").click();
});

$("#dump-file").change(function(e){
    document.getElementById("chose-dump").style.display = "none";
    document.getElementById("change-dump").style.display = "inline-block";
    document.getElementById("upload-dump").style.display = "inline-block";
    document.getElementById("selected-file").style.opacity = "1";

    fileName = e.target.value.split( '\\' ).pop();
    if( fileName )
        document.getElementById("selected-file").innerHTML = fileName;
});

$("#upload-dump").click(function(e) {
    e.preventDefault();
    const url = serverURL + "/analyze";

    var input = document.querySelector('input[type="file"]');
    var data = new FormData()
    data.append('file', input.files[0])

    var token = JSON.parse(localStorage.getItem('token'));

    console.log(token['token']);

    fetch(url, {
        method:"POST",
        headers: {
            "Authorization": "Bearer "+ token['token']
        },
        body: data
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else if (response.status == 401){
            //Login again. Credentials Expired or Wrong Credentials
            throw new Error('Something went wrong');
        } else{
            //Server Error
            throw new Error('Something went wrong');
        }
    })
    .then(data => {
        console.log("received");
        console.log(data);
        localStorage.setItem("analysis_data", JSON.stringify(data));
        window.location.href = "dashboard.html";
    })
    .catch(error => console.error(error));
});