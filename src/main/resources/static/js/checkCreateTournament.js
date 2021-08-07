var golfSwingSound = new Audio("https://ringtons.s3.eu-west-2.amazonaws.com/th5s43yk.mp3");
var numButtons = document.querySelectorAll("button").length;
var timeoutLength = 500; // in milliseconds (perfect timing for above mp3)

var submit = false;



window.addEventListener("load", function(){
        var date = new Date();
        var today = date.toLocaleDateString();
        document.getElementById("date").min = today;
});

function checkEmptyFields(event) {
    var checkTournName = document.getElementById("tournaName").value;
    var checkDate = document.getElementById("date").value;
    var checkTime = document.getElementById("time").value;
    var checkPSlots = document.getElementById("particiSlots").value;


    if (checkTournName == ""){
        document.getElementById("errorTN").innerHTML = "Tournament Name is empty: Please enter a Tournament Name ";
        document.getElementById("errorTN").removeAttribute("hidden");
        return false;
    }
    else if (checkDate == ""){
        document.getElementById("errorD").innerHTML = "Date is empty: Please enter a Date";
        document.getElementById("errorD").removeAttribute("hidden");
        return false;
    }
    else if (checkTime == ""){
        document.getElementById("errorT").innerHTML = "Time is empty: Please enter a Time";
        document.getElementById("errorT").removeAttribute("hidden");
        return false;
    }
    else if (checkPSlots == ""){
        document.getElementById("errorP").innerHTML = "Participant Slots is empty: Please enter a Participant Slots";
        document.getElementById("errorP").removeAttribute("hidden");
        return false;
    } else{
        submit = true;
        return true;
    }
}

function submitForm(target){
    if(target.id == "submitTourna"){ //does check if its for submit button
        if(checkEmptyFields()){
            target.parentElement.submit();
        }
    } else {
        target.parentElement.submit();
    }
}

var numberOfButtons = document.querySelectorAll("button").length;
for(var i = 0; i < numberOfButtons; i++) {
    document.querySelectorAll("button")[i].addEventListener("click", function(event){
        currentButtonClicked = event.target;
        
        event.preventDefault();
        golfSwingSound.play();
        
        setTimeout(submitForm, timeoutLength, event.target);
    });
}