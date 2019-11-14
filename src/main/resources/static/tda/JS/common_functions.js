$("#logout-btn").click(function (e){
    e.preventDefault();
    $($(this).data("target")).modal("show");
});

$("#final-logout-btn").click(function (e){
    e.preventDefault();
    localStorage.clear();
    window.location.href = "index.html";
});