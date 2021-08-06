var userPriority = document.querySelectorAll(".priority")[0].innerText;
var playerCount = Number(document.querySelectorAll(".playerCount")[0].innerText);
var username = document.querySelectorAll(".currentUser")[0].innerText;

document.querySelectorAll(".priority")[0].style.display = "none";
document.querySelectorAll(".playerCount")[0].style.display = "none";
document.querySelectorAll(".currentUser")[0].style.display = "none";


if(userPriority == "GOLFER"){               //hide the delete and publish results buttons
    document.querySelectorAll(".delete")[0].style.display = "none";
    document.querySelectorAll(".publishResults")[0].style.display = "none";
}else if(userPriority == "OWNER"){          //hide the signup button
    document.querySelectorAll(".signup")[0].style.display = "none";
}


var availableSpots = Number(document.querySelectorAll(".slots")[0].innerText);
if(availableSpots == 0){
    //hide the signup button
    document.querySelectorAll(".signup")[0].style.display = "none";
}


for(var i = 0; i < playerCount; i++){
    if(document.querySelectorAll(".accountName")[i].innerText.localeCompare(username) === 0){
        //hide the signup button
        document.querySelectorAll(".signup")[0].style.display = "none";
        break;
    }
}
