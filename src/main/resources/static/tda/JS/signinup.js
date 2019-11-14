const signUpButton = document.getElementById('signUp');
const signInButton = document.getElementById('signIn');
const container = document.getElementById('container');

signUpButton.addEventListener('click', () => {
	container.classList.add("right-panel-active");
});

signInButton.addEventListener('click', () => {
	container.classList.remove("right-panel-active");
});
$(document).ready(function(){
let	username=localStorage.getItem('username')
if(username ==null){
//login or signup
console.log("Login First")
}
else {
	if(analysis_data != null){
		display_analysis(analysis_data);
		
	}else{
		window.location.href = "index.html"
		document.getElementById("thread-dump-title").innerHTML = 'ERROR IN DUMP FILE : CANNOT ANALYZE';
	}
}
})