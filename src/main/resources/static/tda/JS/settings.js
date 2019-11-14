var serverURL = "http://localhost:8080";

function getAnalysis(){

    const url = serverURL + "/getanalysis";
    var token = JSON.parse(localStorage.getItem('token'));
    
    return fetch(url, {
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
        localStorage.setItem("old_analysis", JSON.stringify(data));
        
    })
    .catch(error => console.error(error))
    
}