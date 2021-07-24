var submit = false;

function checkNewValue(event) {
    var checkValue = document.getElementById("value").value;
    console.log("Value:'" + checkValue +"'");
    if (checkValue == ""){
        document.getElementById("error").innerHTML = "Field is Empty";
        return false;
    } else
        submit = true;
        return true;
}

// insert golf sound
var golfSwingSound = new Audio("http://sfxcontent.s3.amazonaws.com/soundfx/GolfSwing.mp3");
var numButtons = document.querySelectorAll("button").length;
var timeoutLength = 350; // in milliseconds (perfect timing for above mp3)
    

function submitForm(target){
    if(target.id == "submitData"){ //does check if its for submit button
        checkNewValue();
        if(submit){
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