function checkDelete(event){
    return confirm("Are you sure you want to delete booking?");        //alert (If ok then true, and if cancel then false)
}

// insert golf sound
var golfSwingSound = new Audio("https://ringtons.s3.eu-west-2.amazonaws.com/th5s43yk.mp3");
var numButtons = document.querySelectorAll("button").length;
var timeoutLength = 500; // in milliseconds (perfect timing for above mp3)
    

function submitForm(target){
    if(target.id == "deleteBooking"){ //does check if its for submit button
        console.log("In if")
        if(checkDelete()){
            target.parentElement.submit();
        }
    } else {
        console.log("In else")
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