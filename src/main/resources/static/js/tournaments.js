
var userPriority = document.querySelectorAll(".priority")[0].innerText;
document.querySelectorAll(".priority")[0].style.display = "none";

if(userPriority == "GOLFER"){               //hide the create button
    document.querySelectorAll(".create")[0].style.display = "none";
}