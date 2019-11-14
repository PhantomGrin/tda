$("#login-button").click(function (e){
    e.preventDefault();

    var username = document.getElementById("login-user").value;
    var password = document.getElementById("login-password").value;

    if(validateEmail(username) && validatePassword(password)){
        loginUser(username, password);
    }else{
        document.getElementById("login-error").innerHTML = "Invalid inputs. Please Correct & Try again";
        document.getElementById("login-alert").classList.remove("hidden-alert");
        throw new Error('Something went wrong');
    }
});

$("#register-button").click(function (e){
    e.preventDefault();

    var fname = document.getElementById("form-fname").value;
    var lname = document.getElementById("form-lname").value;
    var email = document.getElementById("form-email").value;
    var team = document.getElementById("form-team").value;
    var password = document.getElementById("form-password").value;
    var repassword = document.getElementById("form-repassword").value;

    if(validateName(fname) && validateName(lname) && validateEmail(email) && validatePassword(password) && team.length>=2){
        if(password.localeCompare(repassword)==0){
            registerUser(fname, lname, email, team, password);
        }
    }else{
        document.getElementById("reg-strong").innerHTML = "Error in Register!: ";
        document.getElementById("reg-error").innerHTML = "Invalid inputs. Please Correct & Try again";
        document.getElementById("reg-alert").classList.remove("alert-info");
        document.getElementById("reg-alert").classList.add("alert-danger");
        throw new Error('Something went wrong');
    }  
});

function registerUser(fname, lname, email, team, password){
    const url = serverURL + "/auth/registration";

    var formData  = {
        "username": email,
        "password": password,
        "email": email,
        "firstname": fname,
        "lastname":lname,
        "team":team
    }

    fetch(url, {
        method:"POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
    .then(response => {
        if (response.status == 201) {
            loginUser(email, password);
        } else{
            document.getElementById("reg-strong").innerHTML = "Error in Register!: ";
            document.getElementById("reg-error").innerHTML = "Something went wrong. Please Try again";
            document.getElementById("reg-alert").classList.remove("alert-info");
            document.getElementById("reg-alert").classList.add("alert-danger");
            throw new Error('Something went wrong');
        }
    })
    .catch(error => console.error(error))
}

function loginUser(username, password){
    const url = serverURL + "/auth";
    var formData  = {"username": username, "password": password};
    var status;
    
    fetch(url, {
        method:"POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
    .then(response => {
        if (response.ok) {
            localStorage.setItem('sample', 'false');
            return response.json();
        } else if (response.status == 401){
            document.getElementById("login-error").innerHTML = "Invalid Credentials. Check your Email and Password";
            document.getElementById("login-alert").classList.remove("hidden-alert");
            throw new Error('Something went wrong');
        } else{
            document.getElementById("login-error").innerHTML = "Something went wrong. Please Try again";
            document.getElementById("login-alert").classList.remove("hidden-alert");
            throw new Error('Something went wrong');
        }
    })
    .then(data => {
        console.log(data);
        localStorage.setItem("token", JSON.stringify(data));
        localStorage.setItem("username", JSON.stringify(username));
    })
    .then(()=>{
        window.location.href = "upload.html";
    })
    .catch(error => console.error(error))
}

function validateName(name){
    if(name.length < 4) {
        return false;
    } else {
        var regex = /^[a-zA-Z\s]+$/;                
        if(regex.test(name) === false) {
            return false;
        } else {
            return true;
        }
    }
}

function validateEmail(email){
    if(email.length < 4) {
        return false;
    } else {
        // Regular expression for basic email validation
        var regex = /^\S+@\S+\.\S+$/;
        if(regex.test(email) === false) {
            return false;
        } else{
            return true;
        }
    }
}

function validatePassword(password){
    // at least one number, one lowercase and one uppercase letter
    // at least eight characters

    var regex = /(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}/;
    if(regex.test(password) === false) {
        return false;
    } else{
        return true;
    }
}

$("#login-close").click(function(e){
    e.preventDefault();
    document.getElementById("login-alert").classList.add("hidden-alert");
});

$("#reg-close").click(function(e){
    e.preventDefault();
    document.getElementById("reg-alert").classList.add("hidden-alert");
});