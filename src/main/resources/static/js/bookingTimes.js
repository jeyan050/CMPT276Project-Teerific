var golfSwingSound = new Audio("https://ringtons.s3.eu-west-2.amazonaws.com/th5s43yk.mp3");
var numButtons = document.querySelectorAll("button").length;
var timeoutLength = 500; // in milliseconds (perfect timing for above mp3)
    

function submitForm(target){
    target.parentElement.submit();
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



var partySizeForm = document.getElementById("partySizeForm");
partySizeForm.style.display = "block";

document.getElementById("dateTimeForm").addEventListener("click", function() {
    partySizeForm.style.display = "none";
})